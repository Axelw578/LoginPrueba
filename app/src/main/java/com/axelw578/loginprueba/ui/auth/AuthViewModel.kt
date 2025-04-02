package com.axelw578.loginprueba.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axelw578.loginprueba.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var currentUser: FirebaseUser? = authRepository.getCurrentUser()

    fun login(email: String, password: String, onResult: (FirebaseUser?) -> Unit) {
        viewModelScope.launch {
            val user = authRepository.login(email, password)
            onResult(user)
        }
    }

    fun registerUser(
        firstName: String,
        lastName: String,
        username: String,
        gender: String,
        age: String,
        email: String,
        password: String,
        onResult: (Boolean) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userData = hashMapOf(
                        "firstName" to firstName,
                        "lastName" to lastName,
                        "username" to username,
                        "gender" to gender,
                        "age" to age,
                        "email" to email,
                        "uid" to user?.uid
                    )

                    user?.let {
                        FirebaseFirestore.getInstance().collection("users")
                            .document(it.uid)
                            .set(userData)
                            .addOnCompleteListener { firestoreTask ->
                                onResult(firestoreTask.isSuccessful)
                            }
                    } ?: onResult(false)
                } else {
                    onResult(false)
                }
            }
    }


    fun resetPassword(email: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = authRepository.resetPassword(email)
            onResult(result)
        }
    }

    fun logout() {
        authRepository.logout()
    }
    fun signInWithGoogle(idToken: String, onResult: (FirebaseUser?) -> Unit) {
        viewModelScope.launch {
            val user = authRepository.signInWithGoogle(idToken)
            onResult(user)
        }
    }


}
