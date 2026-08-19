package com.example.whatsappclone.presentation.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.whatsappclone.R
import com.example.whatsappclone.presentation.bottomNavigation.BottomNavigation
import com.example.whatsappclone.presentation.chatBox.ChatDesign
import com.example.whatsappclone.presentation.chatBox.ChatListModel
import com.example.whatsappclone.presentation.navigation.Routes
import com.example.whatsappclone.presentation.viewModel.BaseViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(
    navController: NavHostController, homeBaseViewModel: BaseViewModel
) {

    var showPopUp by remember { mutableStateOf(false) }

    val chatData by homeBaseViewModel.chatList.collectAsState()

    val userId = FirebaseAuth.getInstance().currentUser?.uid

    if (userId != null) {

        LaunchedEffect(userId) {
            homeBaseViewModel.loadChatList(userId) { chatList ->
                homeBaseViewModel._chatList.value = chatList
            }
        }
    }

    var showMenu by remember { mutableStateOf(false) }

    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = {showPopUp = true},
            containerColor = colorResource(R.color.light_green),
            contentColor = Color.White,
            modifier = Modifier.padding(65.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.add_chat_icon),
                contentDescription = "Add Chat",
                modifier = Modifier.size(28.dp),
                tint = Color.White
            )
        }
    }, bottomBar = {
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
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .background(Color.White)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {

                var isSearch by remember { mutableStateOf(false) }
                var searchText by remember { mutableStateOf("") }
                var showMenu by remember { mutableStateOf(false) }

                if (isSearch) {
                    TextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                        },
                        placeholder = {
                            Text(text = "Search")
                        },
                        singleLine = true,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(start = 12.dp)
                            .fillMaxWidth(0.8f),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                } else {
                    Text(
                        text = "WhatsApp",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 12.dp)
                    )

                    Row(
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        IconButton(onClick = { }) {
                            Icon(
                                painter = painterResource(id = R.drawable.camera),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.Black
                            )
                        }

                        if (isSearch) {
                            IconButton(onClick = {
                                searchText = ""
                                isSearch = false
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.cross),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.Black
                                )
                            }

                        } else {
                            IconButton(onClick = {
                                isSearch = true
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.search),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.Black
                                )
                            }
                        }

                        IconButton(onClick = {
                            showMenu = !showMenu
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.more),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.Black
                            )

                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false },
                                modifier = Modifier.background(Color.White)
                            ) {
                                DropdownMenuItem(
                                    text = { Text(text = "New Group") },
                                    onClick = { showMenu = false })
                                DropdownMenuItem(
                                    text = { Text(text = "New BroadCast") },
                                    onClick = { showMenu = false })
                                DropdownMenuItem(
                                    text = { Text(text = "Linked Device") },
                                    onClick = { showMenu = false })
                                DropdownMenuItem(
                                    text = { Text(text = "Starred Message") },
                                    onClick = { showMenu = false })
                                DropdownMenuItem(text = { Text(text = "Setting") }, onClick = {
                                    showMenu = false
                                    navController.navigate(Routes.SettingScreen)
                                })

                            }
                        }


                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(8.dp))

            if (showPopUp) {
                AddUserPopUp(
                    onDismiss = { showPopUp = false },
                    onUserAdd = { newUser -> homeBaseViewModel.addChat(userId ?: "", newUser) },
                    homeBaseViewModel = homeBaseViewModel
                )
            }

            LazyColumn {
                items(chatData) { chat ->
                    ChatDesign(chatListModel = chat, onClick = {
                        navController.navigate(
                            Routes.ChatScreen.createRoutes(
                                phoneNumber = chat.phoneNumber ?: "ok"
                            )
                        )
                    }, baseViewModel = homeBaseViewModel)
                }
            }
        }
    }
}

@Composable
fun AddUserPopUp(
    onDismiss: () -> Unit, onUserAdd: (ChatListModel) -> Unit, homeBaseViewModel: BaseViewModel
) {

    var phoneNumber by remember { mutableStateOf("") }
    var userFound by remember {
        mutableStateOf<ChatListModel?>(null)
    }

    var isSearching by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            value = phoneNumber,
            onValueChange = {
                phoneNumber = it
            },
            placeholder = {
                Text(text = "Enter Phone Number")
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
            )

        )

        Row() {

            Button(
                onClick = {
                    isSearching = true
                    homeBaseViewModel.searchUserByPhoneNumber(phoneNumber) { user ->
                        isSearching = false
                        if (user != null) {
                            userFound = user
                        } else {
                            userFound = null
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.light_green))
            ) {
                Text(text = "Search")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.light_green))
            ) {
                Text(text = "Cancel")
            }
        }

        if (isSearching) {
            Text(text = "Searching...")
        }
        userFound?.let {
            Column {
                Text(text = "User Found ${it.name}")
            }

            Button(
                onClick = {
                    onUserAdd(it)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(colorResource(R.color.light_green))

            ) {
                Text(text = "Add to Chat")
            }
        } ?: run {
            if (!isSearching) {
                Text(text = "User Not Found")
            }
        }

    }
}
