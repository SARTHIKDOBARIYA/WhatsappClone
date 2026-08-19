package com.example.whatsappclone.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Routes {

    @Serializable
    data object SplashScreen : Routes()

    @Serializable
    data object HomeScreen : Routes()

    @Serializable
    data object UpdateScreen : Routes()

    @Serializable
    data object CommunitiesScreen : Routes()

    @Serializable
    data object CallScreen : Routes()

    @Serializable
    data object UserRegistrationScreen : Routes()

    @Serializable
    data object UserProfileScreen : Routes()


    @Serializable
    data object SettingScreen : Routes()

    @Serializable
    data object ChatScreen : Routes(){
        fun createRoutes(phoneNumber:String):String{
            return "ChatScreen/$phoneNumber"
        }
    }
}