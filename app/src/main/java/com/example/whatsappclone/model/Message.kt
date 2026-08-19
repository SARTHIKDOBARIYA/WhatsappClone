package com.example.whatsappclone.model

data class Message(
    val senderPhoneNumber: String? = null,
    val message: String = "",
    val timestamp: Long = 0L
)
