package com.catnip.foodfood.repository

import android.net.Uri
import com.catnip.foodfood.data.FirebaseAuthDataSource
import com.catnip.foodfood.model.User
import com.catnip.foodfood.model.toUser
import com.catnip.foodfood.utils.ResultWrapper
import com.catnip.foodfood.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun doLogin(email: String, password: String): Flow<ResultWrapper<Boolean>>

    suspend fun doRegister(
        username: String,
        email: String,
        password: String
    ): Flow<ResultWrapper<Boolean>>

    fun doLogout()

    fun isLoggedIn(): Boolean

    fun getCurrentUser(): User?

    suspend fun updateProfile(
        username: String? = null,
        email: String? = null,
        password: String? = null,
        photoUri: Uri? = null
    ): Flow<ResultWrapper<Boolean>>
}

class UserRepositoryImpl(private val dataSource: FirebaseAuthDataSource) : UserRepository {
    override suspend fun doLogin(email: String, password: String): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.doLogin(email, password) }
    }

    override suspend fun doRegister(
        username: String,
        email: String,
        password: String
    ): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.doRegister(username, email, password) }
    }

    override fun doLogout() {
        dataSource.doLogout()
    }

    override fun isLoggedIn(): Boolean {
        return dataSource.isLoggedIn()
    }

    override fun getCurrentUser(): User? {
        return dataSource.getCurrentUser().toUser()
    }

    override suspend fun updateProfile(
        username: String?,
        email: String?,
        password: String?,
        photoUri: Uri?
    ): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.updateProfile(username, email, password, photoUri) }
    }
}
