package com.example.whatsappclone.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.whatsappclone.presentation.callScreen.CallScreen
import com.example.whatsappclone.presentation.communitiesScreen.CommunityScreen
import com.example.whatsappclone.presentation.homeScreen.HomeScreen
import com.example.whatsappclone.presentation.profile.userProfileSetScreen
import com.example.whatsappclone.presentation.splashScreen.SplashScreen
import com.example.whatsappclone.presentation.updateScreen.UpdateScreen
import com.example.whatsappclone.presentation.userRegistrationScreen.UserRegistrationScreen
import com.example.whatsappclone.presentation.viewModel.BaseViewModel

@Composable
fun WhatsappNavigationSystem(){
    val navController = rememberNavController()

    NavHost(
        startDestination = Routes.SplashScreen,
        navController = navController,
    ){
        composable<Routes.SplashScreen>{
            SplashScreen(navController)
        }
        composable<Routes.HomeScreen>{
val baseViewModel = BaseViewModel()
            HomeScreen(homeBaseViewModel = baseViewModel, navController = navController)
        }
        composable<Routes.UpdateScreen>{
            UpdateScreen(navController)
        }
        composable<Routes.CommunitiesScreen>{
            CommunityScreen(navController)
        }
        composable<Routes.CallScreen>{
            CallScreen(navController)
        }
        composable<Routes.UserRegistrationScreen>{
            UserRegistrationScreen(navController)
        }
        composable<Routes.UserProfileScreen>{
            userProfileSetScreen( navController = navController)
        }

    }
}