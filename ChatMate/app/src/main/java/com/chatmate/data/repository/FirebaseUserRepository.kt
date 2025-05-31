package com.chatmate.data.repository

import com.chatmate.data.model.User
import com.chatmate.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.Clock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseUserRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : UserRepository {

    override suspend fun getCurrentUser(): User? {
        val firebaseUser = firebaseAuth.currentUser ?: return null
        return try {
            val document = firestore.collection("users")
                .document(firebaseUser.uid)
                .get()
                .await()
            document.toObject(User::class.java)?.copy(id = document.id)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun signInUser(name: String): Result<User> {
        return try {
            val result = firebaseAuth.signInAnonymously().await()
            val firebaseUser = result.user ?: return Result.failure(Exception("Failed to sign in"))

            val user = User(
                id = firebaseUser.uid,
                name = name,
                isOnline = true,
                lastSeen = Clock.System.now()
            )

            firestore.collection("users")
                .document(firebaseUser.uid)
                .set(user)
                .await()

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOutUser() {
        val currentUser = getCurrentUser()
        currentUser?.let { user ->
            updateUserStatus(false)
        }
        firebaseAuth.signOut()
    }

    override suspend fun updateUserStatus(isOnline: Boolean) {
        val firebaseUser = firebaseAuth.currentUser ?: return
        try {
            val updates = if (isOnline) {
                mapOf(
                    "isOnline" to true,
                    "lastSeen" to Clock.System.now()
                )
            } else {
                mapOf(
                    "isOnline" to false,
                    "lastSeen" to Clock.System.now()
                )
            }

            firestore.collection("users")
                .document(firebaseUser.uid)
                .update(updates)
                .await()
        } catch (e: Exception) {
            // Handle error
        }
    }

    override fun getUsersFlow(): Flow<List<User>> = callbackFlow {
        val listener = firestore.collection("users")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val users = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(User::class.java)?.copy(id = document.id)
                } ?: emptyList()

                trySend(users)
            }

        awaitClose { listener.remove() }
    }

    override suspend fun getUser(userId: String): User? {
        return try {
            val document = firestore.collection("users")
                .document(userId)
                .get()
                .await()
            document.toObject(User::class.java)?.copy(id = document.id)
        } catch (e: Exception) {
            null
        }
    }
}