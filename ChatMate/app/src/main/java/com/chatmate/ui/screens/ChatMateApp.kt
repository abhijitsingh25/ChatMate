package com.chatmate.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chatmate.presentation.viewmodel.AuthViewModel

@Composable
fun ChatMateApp(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val authUiState by authViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (currentUser != null) "chat_list" else "auth"
    ) {
        composable("auth") {
            AuthScreen(
                uiState = authUiState,
                onSignIn = authViewModel::signIn,
                onClearError = authViewModel::clearError,
                onNavigateToChatList = {
                    navController.navigate("chat_list") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

        composable("chat_list") {
            ChatListScreen(
                onNavigateToChat = { chatRoomId ->
                    navController.navigate("chat/$chatRoomId")
                }
            )
        }

        composable("chat/{chatRoomId}") { backStackEntry ->
            val chatRoomId = backStackEntry.arguments?.getString("chatRoomId") ?: ""
            ChatScreen(
                chatRoomId = chatRoomId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}