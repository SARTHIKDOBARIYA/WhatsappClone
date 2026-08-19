package com.example.whatsappclone.presentation.callScreen

import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.whatsappclone.R

@Composable
fun FloatingActionButton(){
        FloatingActionButton(
            onClick = { /*TODO*/ },
            containerColor = colorResource(R.color.light_green),
            modifier = Modifier.size(65.dp),

            ) {
            Icon(
                painter = painterResource(R.drawable.baseline_photo_camera_24),
                contentDescription = "Add",
                modifier = Modifier.size(28.dp)
            )
        }
}