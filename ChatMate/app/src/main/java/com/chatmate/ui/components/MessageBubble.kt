package com.chatmate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chatmate.data.model.Message
import com.chatmate.ui.theme.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun MessageBubble(
    message: Message,
    isCurrentUser: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isCurrentUser) {
            Spacer(modifier = Modifier.width(8.dp))
        }

        Card(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .wrapContentWidth(),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isCurrentUser) 16.dp else 4.dp,
                bottomEnd = if (isCurrentUser) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isCurrentUser) ChatBubbleSent else ChatBubbleReceived
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                if (!isCurrentUser) {
                    Text(
                        text = message.senderName,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = if (isCurrentUser) ChatBubbleSentText else MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isCurrentUser) ChatBubbleSentText else ChatBubbleReceivedText
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = formatMessageTime(message.timestamp),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isCurrentUser) 
                            ChatBubbleSentText.copy(alpha = 0.7f) 
                        else 
                            ChatBubbleReceivedText.copy(alpha = 0.7f)
                    )

                    if (isCurrentUser) {
                        Spacer(modifier = Modifier.width(4.dp))
                        MessageStatusIcon(
                            status = message.status,
                            color = ChatBubbleSentText.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }

        if (isCurrentUser) {
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
private fun MessageStatusIcon(
    status: com.chatmate.data.model.MessageStatus,
    color: Color
) {
    val icon = when (status) {
        com.chatmate.data.model.MessageStatus.SENT -> "✓"
        com.chatmate.data.model.MessageStatus.DELIVERED -> "✓✓"
        com.chatmate.data.model.MessageStatus.READ -> "✓✓"
    }

    Text(
        text = icon,
        style = MaterialTheme.typography.labelSmall,
        color = if (status == com.chatmate.data.model.MessageStatus.READ) 
            Color(0xFF4CAF50) 
        else 
            color
    )
}

private fun formatMessageTime(timestamp: kotlinx.datetime.Instant): String {
    val messageTime = timestamp.toLocalDateTime(TimeZone.currentSystemDefault())
    return String.format("%02d:%02d", messageTime.hour, messageTime.minute)
}