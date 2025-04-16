package com.example.chatmate.feature.chat

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatmate.model.Message


@Composable
fun ChatScreen(navController: NavController, channelId: String) {

    val viewModel: ChatViewModel = hiltViewModel()

    LaunchedEffect(key1 = true) {
        viewModel.listenForMessages(channelId)
    }

    val messages = viewModel.message.collectAsState()

}

@Composable
fun ChatMessages(
    messages: List<Message>,
    onSendMessage: (String) -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()){
        LazyColumn {
            items(messages) { message ->
                ChatMessage(message = message)
            }
        }
    }

}


@Composable
fun ChatBubble(message: Message) {
    val isCurrentUser = message.senderId == "current_user_id"
    val bubbleColor = if (isCurrentUser)
        Color.BLUE
     else
        Color.GREEN
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        val alignment = if(isCurrentUser) Alignment.CenterStart else Alignment.CenterEnd

            Box(
                contentAlignment = alignment,
                modifier = Modifier
                .padding(8.dp)
                .background(color= bubbleColor, shape = RoundedCornerShape(8.dp))
            ){
                Text(text = message.message,
                    color = Color.WHITE,
                    modifier = Modifier.padding(8.dp))
            }
        }


}

