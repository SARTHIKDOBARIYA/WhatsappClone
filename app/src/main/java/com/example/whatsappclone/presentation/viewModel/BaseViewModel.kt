package com.example.whatsappclone.presentation.viewModel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.whatsappclone.model.Message
import com.example.whatsappclone.presentation.chatBox.ChatListModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.ByteArrayInputStream

class BaseViewModel : ViewModel() {

    fun searchUserByPhoneNumber(phoneNumber: String, callback: (ChatListModel?) -> Unit) {

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            callback(null)
            return
        }

        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.orderByChild("phoneNumber").equalTo(phoneNumber)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.children.first().getValue(ChatListModel::class.java)
                        callback(user)
                    } else {
                        callback(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("BaseViewModel", "Database Error: ${error.message} Details: ${error.details}")
                    callback(null)
                }
            })
    }

    fun getChatForUser(userId: String, callback: (List<ChatListModel>) -> Unit) {

        val chatRef = FirebaseDatabase.getInstance().getReference("user/$userId/chats")
        chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chatList = mutableListOf<ChatListModel>()
                for (chatSnapshot in snapshot.children) {
                    val chat = chatSnapshot.getValue(ChatListModel::class.java)
                    if (chat != null) {
                        chatList.add(chat)
                    }
                }
                callback(chatList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("BaseViewModel", "Database Error: ${error.message}")
                callback(emptyList())
            }
        })
    }

    val _chatList = MutableStateFlow<List<ChatListModel>>(emptyList())
    val chatList = _chatList.asStateFlow()

    init {
        loadChatData()
    }

    private fun loadChatData() {

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId != null) {
            val chatRef = FirebaseDatabase.getInstance().getReference("user/$currentUserId/chats")

            chatRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val chatList = mutableListOf<ChatListModel>()
                    for (chatSnapshot in snapshot.children) {
                        val chat = chatSnapshot.getValue(ChatListModel::class.java)
                        if (chat != null) {
                            chatList.add(chat)
                        }
                    }
                    _chatList.value = chatList
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("BaseViewModel", "Database Error: ${error.message}")
                }
            })
        }
    }

    fun addChat(currentUserPhoneNumber: String, chat: ChatListModel) {

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId != null) {
            val chatRef = FirebaseDatabase.getInstance()
                .getReference("chats")
                .child(currentUserPhoneNumber)
                .child(chat.phoneNumber)

            val chatData = mapOf(
                "name" to chat.name,
                "image" to chat.image
            )

            chatRef.setValue(chatData)
                .addOnSuccessListener {
                    Log.d("BaseViewModel", "Chat added successfully")
                }
                .addOnFailureListener { e ->
                    Log.e("BaseViewModel", "Failed to add chat: ${e.message}")
                }
        } else {
            Log.e("BaseViewModel", "Current user ID is null")
        }
    }

    private val databaseReference = FirebaseDatabase.getInstance().reference

    fun sendMessage(senderPhoneNumber: String, receiverPhoneNumber: String, messageText: String) {

        val messageId = databaseReference.push().key ?: return
        val message = Message(
            senderPhoneNumber = senderPhoneNumber,
            message = messageText,
            timestamp = System.currentTimeMillis()
        )

        databaseReference.child("messages")
            .child(senderPhoneNumber)
            .child(receiverPhoneNumber)
            .child(messageId)
            .setValue(message)

        databaseReference.child("messages")
            .child(receiverPhoneNumber)
            .child(senderPhoneNumber)
            .child(messageId)
            .setValue(message)
    }

    fun getMessages(
        senderPhoneNumber: String,
        receiverPhoneNumber: String,
        onNewMessage: (Message) -> Unit
    ) {
        val messageRef = databaseReference.child("messages")
            .child(senderPhoneNumber)
            .child(receiverPhoneNumber)

        messageRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                    onNewMessage(message)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                Log.e("BaseViewModel", "getMessages error: ${error.message}")
            }
        })
    }

    fun fetchLastMessageForChat(
        senderPhoneNumber: String,
        receiverPhoneNumber: String,
        callback: (String, String) -> Unit
    ) {
        val messageRef = databaseReference.child("messages")
            .child(senderPhoneNumber)
            .child(receiverPhoneNumber)

        messageRef.orderByChild("timestamp").limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val lastMessage = snapshot.children.firstOrNull()
                            ?.child("message")?.value as? String

                        val timeStamp = snapshot.children.firstOrNull()
                            ?.child("timestamp")?.value?.toString()

                        callback(lastMessage ?: "No message", timeStamp ?: "--:--")
                    } else {
                        callback("No message", "--:--")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("BaseViewModel", "fetchLastMessage error: ${error.message}")
                    callback("No message", "--:--")
                }
            })
    }

    fun loadChatList(
        currentUserPhoneNumber: String,
        onChatListLoaded: (List<ChatListModel>) -> Unit
    ) {
        val chatList = mutableListOf<ChatListModel>()
        val chatRef = FirebaseDatabase.getInstance().reference
            .child("chats")
            .child(currentUserPhoneNumber)

        chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach { child ->
                        val phoneNumber = child.key ?: return@forEach
                        val name = child.child("name").value as? String ?: "Unknown"
                        val image = child.child("image").value as? String
                        val profileImage = image?.let { decodeBase64toBitmap(it) }

                        fetchLastMessageForChat(currentUserPhoneNumber, phoneNumber) { lastMessage, timeStamp ->
                            chatList.add(
                                ChatListModel(
                                    name = name,
                                    phoneNumber = phoneNumber,
                                    image = profileImage,
                                    message = lastMessage,
                                    time = timeStamp
                                )
                            )

                            if (chatList.size == snapshot.childrenCount.toInt()) {
                                onChatListLoaded(chatList)
                            }
                        }
                    }
                } else {
                    onChatListLoaded(emptyList())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("BaseViewModel", "loadChatList error: ${error.message}")
                onChatListLoaded(emptyList())
            }
        })
    }

    private fun decodeBase64toBitmap(base64Image: String): Bitmap? {
        return try {
            val decodedByte = Base64.decode(base64Image, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun base64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedByte = Base64.decode(base64String, Base64.DEFAULT)
            val inputStream = ByteArrayInputStream(decodedByte)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}