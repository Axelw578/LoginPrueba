package com.axelw578.loginprueba.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    private val firestore = FirebaseFirestore.getInstance()

    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

    suspend fun login(email: String, password: String): FirebaseUser? {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception) {
            null
        }
    }

    suspend fun register(email: String, password: String): FirebaseUser? {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception) {
            null
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    // Función para guardar datos adicionales del usuario en Firestore.
    // Se asume que existe una colección "users" en Firestore.
    suspend fun saveUserData(
        userId: String,
        name: String,
        lastName: String,
        age: String,
        gender: String,
        username: String,
        email: String
    ): Boolean {
        val userMap = hashMapOf(
            "name" to name,
            "lastName" to lastName,
            "age" to age,
            "gender" to gender,
            "username" to username,
            "email" to email
        )
        return try {
            firestore.collection("users").document(userId).set(userMap).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Función para enviar un correo de reinicio de contraseña.
    suspend fun resetPassword(email: String): Boolean {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            false
        }


    }

    suspend fun signInWithGoogle(idToken: String): FirebaseUser? {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            result.user
        } catch (e: Exception) {
            null
        }
    }

}
