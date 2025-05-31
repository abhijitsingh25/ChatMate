package com.chatmate.data.repository

import com.chatmate.data.model.ChatRoom
import com.chatmate.data.model.Message
import com.chatmate.data.model.MessageStatus
import com.chatmate.domain.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.Clock
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseChatRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : ChatRepository {

    override fun getChatRoomsFlow(): Flow<List<ChatRoom>> = callbackFlow {
        val currentUserId = firebaseAuth.currentUser?.uid ?: return@callbackFlow

        val listener = firestore.collection("chatRooms")
            .whereArrayContains("participants", currentUserId)
            .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val chatRooms = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(ChatRoom::class.java)?.copy(id = document.id)
                } ?: emptyList()

                trySend(chatRooms)
            }

        awaitClose { listener.remove() }
    }

    override fun getMessagesFlow(chatRoomId: String): Flow<List<Message>> = callbackFlow {
        val listener = firestore.collection("chatRooms")
            .document(chatRoomId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val messages = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(Message::class.java)?.copy(id = document.id)
                } ?: emptyList()

                trySend(messages)
            }

        awaitClose { listener.remove() }
    }

    override suspend fun sendMessage(message: Message): Result<Unit> {
        return try {
            val messageId = UUID.randomUUID().toString()
            val messageWithId = message.copy(
                id = messageId,
                timestamp = Clock.System.now()
            )

            // Add message to messages subcollection
            firestore.collection("chatRooms")
                .document(message.chatRoomId)
                .collection("messages")
                .document(messageId)
                .set(messageWithId)
                .await()

            // Update chat room's last message
            firestore.collection("chatRooms")
                .document(message.chatRoomId)
                .update(
                    mapOf(
                        "lastMessage" to messageWithId,
                        "lastMessageTimestamp" to messageWithId.timestamp
                    )
                )
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createChatRoom(participantIds: List<String>): Result<ChatRoom> {
        return try {
            val chatRoomId = UUID.randomUUID().toString()
            val chatRoom = ChatRoom(
                id = chatRoomId,
                participants = participantIds,
                createdAt = Clock.System.now(),
                lastMessageTimestamp = Clock.System.now()
            )

            firestore.collection("chatRooms")
                .document(chatRoomId)
                .set(chatRoom)
                .await()

            Result.success(chatRoom)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markMessagesAsRead(chatRoomId: String, userId: String) {
        try {
            val messagesQuery = firestore.collection("chatRooms")
                .document(chatRoomId)
                .collection("messages")
                .whereNotEqualTo("senderId", userId)
                .whereNotEqualTo("status", MessageStatus.READ)
                .get()
                .await()

            val batch = firestore.batch()
            messagesQuery.documents.forEach { document ->
                batch.update(document.reference, "status", MessageStatus.READ)
            }
            batch.commit().await()
        } catch (e: Exception) {
            // Handle error
        }
    }

    override suspend fun setTypingStatus(chatRoomId: String, userId: String, isTyping: Boolean) {
        try {
            if (isTyping) {
                firestore.collection("chatRooms")
                    .document(chatRoomId)
                    .update("isTyping", com.google.firebase.firestore.FieldValue.arrayUnion(userId))
                    .await()
            } else {
                firestore.collection("chatRooms")
                    .document(chatRoomId)
                    .update("isTyping", com.google.firebase.firestore.FieldValue.arrayRemove(userId))
                    .await()
            }
        } catch (e: Exception) {
            // Handle error
        }
    }

    override suspend fun updateMessageStatus(messageId: String, status: MessageStatus) {
        // Implementation for updating message status
        // This would typically be called by the receiving client
    }
}