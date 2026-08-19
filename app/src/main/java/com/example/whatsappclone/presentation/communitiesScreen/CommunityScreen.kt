package com.example.whatsappclone.presentation.communitiesScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
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
fun CommunityScreen(
    navController: NavHostController
){

    val SampleCommunityData = listOf(
        Communities(
            R.drawable.sharukh_khan,
            name = "Sarukh khan",
            memberCount = "1000 members"
        ),
        Communities(
            R.drawable.sharadha_kapoor,
            name = "sharadha_kapoor",
            memberCount = "1000 members"
        ),
        Communities(
            R.drawable.salman_khan,
            name = "Salman khan",
            memberCount = "1000 members"
        ),
    )

    Scaffold(
        topBar = {
            CommunityTopBar()
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
        }
    ) {

        Column(
            modifier = Modifier.padding(it)
        ) {

            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.light_green)
                ),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ){

                Text(
                    text = "Start a new community",
                    color = colorResource(R.color.white),
                    fontSize = 16.sp
                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your Communities",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp , vertical = 8.dp)
            )

            LazyColumn(){
                items(SampleCommunityData){ it ->
                    CommunityItemDesign(communities = it)
                }
            }
        }
    }
}

