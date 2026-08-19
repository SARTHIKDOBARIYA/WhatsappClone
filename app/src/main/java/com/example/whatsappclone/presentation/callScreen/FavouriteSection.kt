package com.example.whatsappclone.presentation.callScreen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whatsappclone.R

@Composable
@Preview(showSystemUi = true , showBackground = true)
fun FavouriteSection(){

    val SampleFavoutie = listOf(
        FavouriteContact(
            R.drawable.rajkummar_rao,
            name = "Rajkummar Rao"
        ),
        FavouriteContact(
            R.drawable.sharukh_khan,
            name = "Sarukh khan"
        ),
        FavouriteContact(
            R.drawable.sharadha_kapoor,
            name = "sharadha_kapoor"
        ),
        FavouriteContact(
            R.drawable.salman_khan,
            name = "Salman khan"
        ),
        FavouriteContact(
            R.drawable.rajkummar_rao,
            name = "Rajkummar Rao"
        ),
    )

    Column(
        modifier = Modifier.padding(start = 16.dp , bottom = 8.dp)
    ){
        Text(
            text = "Favourites",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
        ){
            SampleFavoutie.forEach{ it ->
                FavouriteItem(favouriteContact = it)
            }
        }
    }
}

data class FavouriteContact(
    val img : Int ,
    val name : String
)