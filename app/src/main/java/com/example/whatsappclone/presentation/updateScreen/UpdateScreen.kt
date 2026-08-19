package com.example.whatsappclone.presentation.updateScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.whatsappclone.R
import com.example.whatsappclone.presentation.bottomNavigation.BottomNavigation
import com.example.whatsappclone.presentation.navigation.Routes

@Composable
fun UpdateScreen(
    navController: NavHostController
){

    val sampleStatus = listOf(
        StatusData(
            R.drawable.sharukh_khan,
            name = "Sarukh khan",
            time = "10:00 AM"
        ),
        StatusData(
            R.drawable.sharadha_kapoor,
            name = "sharadha_kapoor",
            time = "10:00 AM"
        ),
        StatusData(
            R.drawable.salman_khan,
            name = "Salman khan",
            time = "10:00 AM"
        ),
        StatusData(
            R.drawable.sharukh_khan,
            name = "Sarukh khan",
            time = "10:00 AM"
        ),

    )

    val sampleChannels = listOf(
        Channels(
            R.drawable.sharukh_khan,
            name = "Sarukh khan",
            description = "Hello"
        ),
        Channels(
            R.drawable.sharadha_kapoor,
            name = "sharadha_kapoor",
            description = "Hello"
        ),
        Channels(
            R.drawable.salman_khan,
            name = "Salman khan",
            description = "Hello"
        ),
    )

    Scaffold(

//        floating Action Button is used like in Whatsapp we saw green chat button which is located on main Screen bottom right above
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                containerColor = colorResource(R.color.light_green),
                modifier = Modifier.size(65.dp),

            ){
                Icon(
                    painter = painterResource(R.drawable.baseline_photo_camera_24),
                    contentDescription = "Add",
                    modifier = Modifier.size(28.dp)
                )
            }
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
        } ,
        topBar = {
            TopBar()
        }
    ){
        Column(
            modifier = Modifier.padding(it)
                .fillMaxSize().verticalScroll(rememberScrollState())
        ){
            Text(
                text = "Status",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 12.dp , vertical = 8.dp)
            )
            
            MyStatus()

            sampleStatus.forEach{data ->
                StatusItem(statusData = data)
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider()

            Text(
                text = "Channels",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 12.dp , vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.padding(horizontal = 16.dp , vertical = 8.dp)
            ){

                Text(
                    text = "stay update on topics that matter to you.find channels to follow below",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Find Channels to follow"
                )

            }

            Spacer(modifier = Modifier.height(16.dp))

            sampleChannels.forEach{data ->
                ChannelItemDesign(channel = data)
            }

        }
    }
}