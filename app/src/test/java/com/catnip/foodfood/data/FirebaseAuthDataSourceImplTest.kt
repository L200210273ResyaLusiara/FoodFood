package com.catnip.foodfood.data

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class FirebaseAuthDataSourceImplTest{
    @MockK
    private lateinit var mockFirebaseAuth: FirebaseAuth

    private lateinit var firebaseAuthDataSource: FirebaseAuthDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        firebaseAuthDataSource = FirebaseAuthDataSourceImpl(mockFirebaseAuth)
    }

    private fun mockTaskVoid(exception: Exception? = null): Task<Void> {
        val task: Task<Void> = mockk(relaxed = true)
        every { task.isComplete } returns true
        every { task.exception } returns exception
        every { task.isCanceled } returns false
        val relaxedVoid: Void = mockk(relaxed = true)
        every { task.result } returns relaxedVoid
        return task
    }

    private fun mockTask(exception: Exception? = null): Task<AuthResult> {
        val task: Task<AuthResult> = mockk(relaxed = true)
        every { task.isComplete } returns true
        every { task.exception } returns exception
        every { task.isCanceled } returns false

        every { task.result } returns mockk()
        val firebaseUser: FirebaseUser = mockk()
        every { task.result.user } returns firebaseUser
        every {
            firebaseUser.updateProfile(any())
        } returns mockTaskVoid()
        return task
    }

    @Test
    fun doLogin(){
        val email = "test@test.com"
        val password = "test1234"

        val task: Task<AuthResult> = mockTask()

        every {
            mockFirebaseAuth.signInWithEmailAndPassword(email, password)
        } returns task

        runTest {
            val result = firebaseAuthDataSource.doLogin(email, password)
            assertTrue(result)
        }
    }

    @Test
    fun doRegister(){
        val email = "test@test.com"
        val password = "test1234"
        val username = "test123"

        val task: Task<AuthResult> = mockTask()

        every {
            mockFirebaseAuth.createUserWithEmailAndPassword(email, password)
        } returns task

        runTest {
            val result = firebaseAuthDataSource.doRegister(username, email, password)
            assertTrue(result)
        }
    }

    @Test
    fun updateProfile() {
        val email = "test@test.com"
        val password = "test1234"
        val username = "test123"
        val uri = Uri.parse("test")

        val firebaseUser:FirebaseUser = mockk()
        every { mockFirebaseAuth.currentUser } returns firebaseUser
        every {
            firebaseUser.updateProfile(any())
        } returns mockTaskVoid()
        every {
            firebaseUser.updateEmail(email)
        } returns mockTaskVoid()
        every {
            firebaseUser.updatePassword(password)
        } returns mockTaskVoid()
        runTest {
            firebaseAuthDataSource.updateProfile(username, email, password, uri)
            verify { firebaseUser.updateProfile(any()) }
        }

    }

    @Test
    fun getCurrentUser() {
        val mockUser = mockk<FirebaseUser>()
        every { mockFirebaseAuth.currentUser } returns mockUser
        val result = firebaseAuthDataSource.getCurrentUser()
        verify { mockFirebaseAuth.currentUser }
        assertEquals(mockUser, result)
    }

    @Test
    fun isLoggedIn() {
        every { mockFirebaseAuth.currentUser } returns mockk()
        val result = firebaseAuthDataSource.isLoggedIn()
        assertTrue(result)
    }
}