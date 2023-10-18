package com.catnip.foodfood.data

import android.net.Uri
import com.catnip.foodfood.model.toUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface FirebaseAuthDataSource {
    @Throws(exceptionClasses = [Exception::class])
    suspend fun doLogin(email: String, password: String): Boolean

    @Throws(exceptionClasses = [Exception::class])
    suspend fun doRegister(username: String, email: String, password: String): Boolean

    suspend fun updateProfile(
        username: String? = null,
        email: String? = null,
        password: String? = null,
        photoUri: Uri? = null
    ): Boolean

    fun doLogout(): Boolean

    fun isLoggedIn(): Boolean

    fun getCurrentUser(): FirebaseUser?
}

class FirebaseAuthDataSourceImpl(private val firebaseAuth: FirebaseAuth) : FirebaseAuthDataSource {

    @Throws(exceptionClasses = [Exception::class])
    override suspend fun doLogin(email: String, password: String): Boolean {
        val loginResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return loginResult.user != null
    }

    @Throws(exceptionClasses = [Exception::class])
    override suspend fun doRegister(username: String, email: String, password: String): Boolean {
        val registerResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        registerResult.user?.updateProfile(
            userProfileChangeRequest {
                displayName = username
            }
        )?.await()
        return registerResult.user != null
    }

    override suspend fun updateProfile(
        username: String?,
        email: String?,
        password: String?,
        photoUri: Uri?
    ): Boolean {
        getCurrentUser()?.updateProfile(
            userProfileChangeRequest {
                username?.let { displayName = username }
                photoUri?.let { this.photoUri = it }
                email?.let { updateEmail(it) }
                password?.let { updatePassword(it) }
            }
        )?.await()
        return true
    }

    private suspend fun updatePassword(newPassword: String): Boolean {
        getCurrentUser()?.updatePassword(newPassword)?.await()
        return true
    }

    private suspend fun updateEmail(newEmail: String): Boolean {
        getCurrentUser()?.updateEmail(newEmail)?.await()
        return true
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override fun doLogout(): Boolean {
        Firebase.auth.signOut()
        return true
    }

    override fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}