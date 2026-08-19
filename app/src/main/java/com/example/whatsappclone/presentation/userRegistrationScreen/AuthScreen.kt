package com.example.whatsappclone.presentation.userRegistrationScreen

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.whatsappclone.R
import com.example.whatsappclone.presentation.navigation.Routes
import com.example.whatsappclone.presentation.viewModel.AuthState
import com.example.whatsappclone.presentation.viewModel.PhoneAuthViewModel
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun UserRegistrationScreen(
    navController: NavHostController,
    phoneAuthViewModel: PhoneAuthViewModel = hiltViewModel()
) {

    val authState by phoneAuthViewModel.authState.collectAsState()
    val context = LocalContext.current
    val activity = context as Activity


    var expanded by remember { mutableStateOf(false) }

    var selectedCountry by remember { mutableStateOf("India") }

    var countryCode by remember { mutableStateOf("+91") }

    var phoneNumber by remember { mutableStateOf("") }

    var otp by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Enter your phone Number",
            fontSize = 20.sp,
            color = colorResource(R.color.dark_green),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row {

            Text(
                text = "whatsApp will need to verify your phone number",
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "what's", color = colorResource(R.color.dark_green)
            )
        }

        Text(
            text = "my number?",
            color = colorResource(R.color.dark_green),
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.width(230.dp)
            ) {
                Text(
                    text = selectedCountry,
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Arrow",
                    modifier = Modifier.align(Alignment.CenterEnd),
                    tint = colorResource(R.color.dark_green)
                )

            }
        }

        HorizontalDivider(
            color = colorResource(R.color.light_green),
            thickness = 2.dp,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("India", "USA", "China", "UK", "Germany", "Australia").forEach { country ->
                DropdownMenuItem(text = {
                    Text(text = country)
                }, onClick = {
                    selectedCountry = country
                    expanded = false
                })
            }
        }

        when (authState) {

            is AuthState.CodeSent, is AuthState.Loading, is AuthState.ideal -> {

                if (authState is AuthState.CodeSent) {
                    verificationId = (authState as AuthState.CodeSent).verificationId

                }

                if (verificationId == null) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextField(
                            value = countryCode,
                            onValueChange = { countryCode = it },
                            modifier = Modifier.width(70.dp),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = colorResource(R.color.light_green),
                            ),
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        TextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            singleLine = true,
                            placeholder = {
                                Text(
                                    text = "Phone Number",
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (phoneNumber.isNotEmpty()) {
                                val phoneNumberWithCountryCode = "$countryCode+$phoneNumber"
                                phoneAuthViewModel.sendVerificationCode(
                                    phoneNumberWithCountryCode,
                                    activity
                                )

                            } else {
                                Toast.makeText(
                                    context,
                                    "Please enter your phone number",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.dark_green),
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Send OTP")
                    }

                    if (authState is AuthState.Loading) {

                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator()
                    }

                }
                else {
                    // OTP input UI

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Enter OTP",
                        fontSize = 20.sp,
                        color = colorResource(R.color.dark_green),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = otp,
                        onValueChange = { otp = it },
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = "Enter OTP",
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (otp.isNotEmpty() && verificationId != null) {
                                phoneAuthViewModel.verifyCode(otp, activity)
                            }else{
                                Toast.makeText(context, "Please enter a valid OTP", Toast.LENGTH_SHORT).show()
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.dark_green),
                            contentColor = Color.White
                        )

                    ) {
                        Text(text = "Verify OTP")
                    }

                    if(authState is AuthState.Loading){

                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator()
                    }
                }
            }

            is AuthState.Success -> {

                Log.d("PhoneAuth", "User is successfully authenticated")

                phoneAuthViewModel.resetAuthState()

                navController.navigate(Routes.UserProfileScreen){
                    popUpTo<Routes.UserRegistrationScreen>{
                        inclusive = true
                    }
                }
            }

            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_SHORT).show()
                phoneAuthViewModel.resetAuthState()
            }

        }

    }

}