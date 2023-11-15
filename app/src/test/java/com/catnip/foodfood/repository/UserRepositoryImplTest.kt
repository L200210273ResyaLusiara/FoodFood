package com.catnip.foodfood.repository

import android.net.Uri
import app.cash.turbine.test
import com.catnip.foodfood.data.FirebaseAuthDataSource
import com.catnip.foodfood.model.Food
import com.catnip.foodfood.model.User
import com.catnip.foodfood.model.toUser
import com.catnip.foodfood.utils.ResultWrapper
import com.catnip.foodfood.utils.proceedFlow
import com.google.firebase.auth.FirebaseUser
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest{
    @MockK
    lateinit var firebaseDataSource: FirebaseAuthDataSource

    private lateinit var repository: UserRepository

    val mockUser = User(
        username = "test",
        email = "test@test.com",
        photoUrl = "test.com"
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = UserRepositoryImpl(firebaseDataSource)
    }
    @Test
    fun `doLogin loading`() {
        val email = "test@test.com"
        val password = "test1234"
        coEvery { firebaseDataSource.doLogin(any(),any()) } returns true
        runTest {
            repository.doLogin(email,password)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(110)
                    val result = expectMostRecentItem()
                    Assert.assertTrue(result is ResultWrapper.Loading)
                    coVerify{ firebaseDataSource.doLogin(any(),any()) }
                }
        }
    }
    @Test
    fun `doLogin success`() {
        val email = "test@test.com"
        val password = "test1234"
        coEvery { firebaseDataSource.doLogin(any(),any()) } returns true
        runTest {
            repository.doLogin(email,password)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    Assert.assertTrue(result is ResultWrapper.Success)
                    Assert.assertEquals(result.payload, true)
                    coVerify { firebaseDataSource.doLogin(any(),any()) }
                }
        }
    }
    @Test
    fun `doRegister loading`() {
        val username = "test123"
        val email = "test@test.com"
        val password = "test1234"
        coEvery { firebaseDataSource.doRegister(any(),any(),any()) } returns true
        runTest {
            repository.doRegister(username,email,password)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(110)
                    val result = expectMostRecentItem()
                    Assert.assertTrue(result is ResultWrapper.Loading)
                    coVerify{ firebaseDataSource.doRegister(any(),any(),any())   }
                }
        }
    }
    @Test
    fun `doRegister success`() {
        val username = "test123"
        val email = "test@test.com"
        val password = "test1234"
        coEvery { firebaseDataSource.doRegister(any(),any(),any()) } returns true
        runTest {
            repository.doRegister(username,email,password)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    Assert.assertTrue(result is ResultWrapper.Success)
                    Assert.assertEquals(result.payload, true)
                    coVerify { firebaseDataSource.doRegister(any(),any(),any()) }
                }
        }
    }
    @Test
    fun isLoggedIn(){
        every { firebaseDataSource.isLoggedIn() } returns true
        val result = repository.isLoggedIn()
        verify { firebaseDataSource.isLoggedIn() }
        Assert.assertTrue(result)
    }
    @Test
    fun doLogout(){
        every { firebaseDataSource.doLogout() } returns Unit
        repository.doLogout()
        verify { firebaseDataSource.doLogout() }
    }
    @Test
    fun getCurrentUser() {
        val user:FirebaseUser = mockk()
        every { firebaseDataSource.getCurrentUser() } returns user

        every { user.displayName } returns mockUser.username
        every { user.email } returns mockUser.email
        every { user.photoUrl.toString() } returns mockUser.photoUrl

        val result = repository.getCurrentUser()
        verify { firebaseDataSource.getCurrentUser() }
        Assert.assertEquals(mockUser, result)
    }
    @Test
    fun `updateProfile loading`() {
        val username = "test123"
        val email = "test@test.com"
        val password = "test1234"
        val photoUri = Uri.parse("test.com")
        coEvery { firebaseDataSource.updateProfile(any(),any(),any(),any()) } returns true
        runTest {
            repository.updateProfile(username,email,password,photoUri)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(110)
                    val result = expectMostRecentItem()
                    Assert.assertTrue(result is ResultWrapper.Loading)
                    coVerify{ firebaseDataSource.updateProfile(any(),any(),any(),any())   }
                }
        }
    }
    @Test
    fun `updateProfile success`() {
        val username = "test123"
        val email = "test@test.com"
        val password = "test1234"
        val photoUri = Uri.parse("test.com")
        coEvery { firebaseDataSource.updateProfile(any(),any(),any(),any()) } returns true
        runTest {
            repository.updateProfile(username,email,password,photoUri)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    Assert.assertTrue(result is ResultWrapper.Success)
                    Assert.assertEquals(result.payload, true)
                    coVerify { firebaseDataSource.updateProfile(any(),any(),any(),any()) }
                }
        }
    }
}