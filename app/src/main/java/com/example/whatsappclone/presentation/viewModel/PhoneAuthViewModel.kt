package com.example.whatsappclone.presentation.viewModel

import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.whatsappclone.model.PhoneAuthUser
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class PhoneAuthViewModel @Inject constructor(
    private val fireBaseAuth: FirebaseAuth,
    private val database: FirebaseDatabase

) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.ideal)
    val authState = _authState.asStateFlow()

    // store detail in node
    private val userRef = database.reference.child("users")

    fun sendVerificationCode(phoneNumber: String, activity: Activity) {
        _authState.value = AuthState.Loading
        val options = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithCredential(credential, context = activity)
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                _authState.value = AuthState.Error(exception.message.toString())
                Log.e("PhoneAuth", "onVerificationFailed: ${exception.message}")
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(id, token)

                Log.d("PhoneAuth", "onCodeSent triggered: $id")

                _authState.value = AuthState.CodeSent(id)
            }

        }
        val phoneAuthOptions = PhoneAuthOptions.newBuilder(fireBaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(
                60L,
                java.util.concurrent.TimeUnit.SECONDS
            )  // wait 60 second for verification code
            .setActivity(activity)
            .setCallbacks(options)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential, context: Activity) {
        _authState.value = AuthState.Loading
        fireBaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(context) { task ->
                if (task.isSuccessful) {
                    val user = fireBaseAuth.currentUser
                    val phoneAuthUser = PhoneAuthUser(
                        userId = user?.uid.toString(),
                        phoneNumber = user?.phoneNumber.toString()
                    )
                    markUserAsSignedIn(context)
                    _authState.value = AuthState.Success(phoneAuthUser)

                    fetchUserProfile(user?.uid.toString())
                } else {
                    _authState.value = AuthState.Error(task.exception?.message.toString())
                }

            }
    }

    private fun markUserAsSignedIn(context: Activity) {
        val sharedPreferences = context.getSharedPreferences("user_data", Activity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_signed_in", true)
        editor.apply()
    }

    private fun fetchUserProfile(userId: String) {
        val userRef = userRef.child(userId)
        userRef.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                // method 1
//                val name = dataSnapshot.child("name").value.toString()
//                val status = dataSnapshot.child("status").value.toString()
//                val profileImage = dataSnapshot.child("profileImage").value.toString()
//                val phoneAuthUser = PhoneAuthUser(
//                    userId = userId,
//                    name = name,
//                    status = status,
//                    profileImage = profileImage
//                )

                // method 2
                val phoneAuthUser = dataSnapshot.getValue(PhoneAuthUser::class.java)
                if(phoneAuthUser != null) {
                    _authState.value = AuthState.Success(phoneAuthUser)
                }
            } else {
                _authState.value = AuthState.Error("User profile not found")
            }
        }
            .addOnFailureListener { exception ->
                _authState.value = AuthState.Error(exception.message.toString())
            }

    }

    fun verifyCode(otp: String, context: Activity) {
        val  currentAuthState = _authState.value

        if(currentAuthState !is AuthState.CodeSent || currentAuthState.verificationId.isEmpty()) {

            Log.d("PhoneAuth", "Attempting to verify code without a valid verification ID")
            _authState.value = AuthState.Error("Invalid verification ID")
            return
        }

        val credential = PhoneAuthProvider.getCredential(currentAuthState.verificationId, otp)
        signInWithCredential(credential, context)
    }

    fun saveUserProfile(userId:String, name:String, status:String, profileImage: Bitmap?){

        val database = FirebaseDatabase.getInstance()

        val encodedImage = profileImage?.let { convertedBitmapToBase64(it) }
        val user = PhoneAuthUser(
            userId = userId,
            name = name,
            status = status,
            phoneNumber = Firebase.auth.currentUser?.phoneNumber?:"",
            profileImage = encodedImage
        )

        userRef.child(userId).setValue(user)

    }

    private fun convertedBitmapToBase64(bitmap: Bitmap): String {

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        val bytes = byteArrayOutputStream.toByteArray()
        return android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)
    }

    fun resetAuthState(){
        _authState.value = AuthState.ideal
    }

    fun signOut(activity : Activity) {
        fireBaseAuth.signOut()
        val sharedPreferences = activity.getSharedPreferences("user_data", Activity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_signed_in", false)
        editor.apply()


    }
}

sealed class AuthState {
    object ideal : AuthState()  //initial state
    object Loading : AuthState() //
    data class CodeSent(val verificationId: String) : AuthState() // when user click on otp button
    data class Success(val user: PhoneAuthUser) :
        AuthState() // when user is successfully authenticated

    data class Error(val message: String) : AuthState()
}