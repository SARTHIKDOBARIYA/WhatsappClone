package com.example.whatsappclone.presentation.callScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.whatsappclone.R
import com.example.whatsappclone.presentation.bottomNavigation.BottomNavigation
import com.example.whatsappclone.presentation.navigation.Routes

@Composable
fun CallScreen(
    navController: NavHostController
){

    var callList = listOf(
        Call(
            R.drawable.rajkummar_rao,
            name = "Rajkummar Rao",
            time = "today 10:00 AM",
            isMissedCall = true
        ),
        Call(
            R.drawable.sharukh_khan,
            name = "Sarukh khan",
            time = "Yesterday 10:00 AM",
            isMissedCall = false
        ),
        Call(
            R.drawable.sharadha_kapoor,
            name = "sharadha_kapoor",
            time = "Yesterday 10:00 AM",
            isMissedCall = true
        ),
        Call(
            R.drawable.salman_khan,
            name = "Salman khan",
            time = "Yesterday 10:00 AM",
            isMissedCall = false
        ),
    )

    Scaffold(
        topBar = {
            CallScreenTopBar()
        },
        bottomBar = {
            BottomNavigation(
                navController = navController,
                selectedItem = 0,
                onClick = {index ->
                    when (index){
                        0 -> {
                            navController.navigate(Routes.HomeScreen)
                        }
                        1 -> {
                            navController.navigate(Routes.UpdateScreen)
                        }
                        2 -> {
                            navController.navigate(Routes.CommunitiesScreen)
                        }
                        3 -> {
                            navController.navigate(Routes.CallScreen)
                        }
                    }

                }
            )
        },
        floatingActionButton = {
            FloatingActionButton()
        }
    ){
        Column(modifier = Modifier.padding(it)) {
            Spacer(modifier = Modifier.height(16.dp))

            FavouriteSection()

            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.light_green)),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ){
                Text(
                    text = "Start a new Call",
                    color = colorResource(R.color.white),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Recent Calls",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp , vertical = 8.dp)
            )

            LazyColumn(){
                items(callList) { it ->
                    CallItemDesign(it)
                }
            }
        }
    }
}