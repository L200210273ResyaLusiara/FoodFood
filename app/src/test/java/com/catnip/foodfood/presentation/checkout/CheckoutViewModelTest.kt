package com.catnip.foodfood.presentation.checkout

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.catnip.foodfood.model.User
import com.catnip.foodfood.presentation.fragmentcart.CartViewModel
import com.catnip.foodfood.repository.CartRepository
import com.catnip.foodfood.repository.UserRepository
import com.catnip.foodfood.utils.MainCoroutineRule
import com.catnip.foodfood.utils.ResultWrapper
import com.catnip.foodfood.utils.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class CheckoutViewModelTest{
    @MockK
    private lateinit var repoCart: CartRepository
    @MockK
    private lateinit var repoUser: UserRepository

    private lateinit var viewModel: CheckoutViewModel

    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule : TestRule = MainCoroutineRule(UnconfinedTestDispatcher())

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        coEvery { repoCart.getCarts()} returns flow{
            emit(
                ResultWrapper.Success(
                    Pair(
                        listOf(
                            mockk(relaxed = true),
                            mockk(relaxed = true)
                        ),
                        127000
                    )
                )
            )
        }
        val userMock:User = mockk(relaxed = true)
        every { userMock.username } returns "TEST"

        coEvery { repoUser.getCurrentUser() } returns userMock

        viewModel= spyk(CheckoutViewModel(repoCart,repoUser))

        val updateResultMock = flow {
            emit(ResultWrapper.Success(true))
        }

        coEvery { repoCart.decreaseCart(any()) } returns updateResultMock
        coEvery { repoCart.setCartNotes(any()) } returns updateResultMock
        coEvery { repoCart.increaseCart(any()) } returns updateResultMock
        coEvery { repoCart.deleteCart(any()) } returns updateResultMock
        coEvery { repoCart.deleteAll() } returns Unit
        coEvery { repoCart.order(any(),any())} returns updateResultMock
    }

    @Test
    fun `test cart data`(){
        val result = viewModel.cartList.getOrAwaitValue()
        Assert.assertEquals(result.payload?.first?.size, 2)
        Assert.assertEquals(result.payload?.second, 127000)
    }
    @Test
    fun `test decrease cart`(){
        viewModel.decreaseCart(mockk())
        coVerify { repoCart.decreaseCart(any()) }
    }
    @Test
    fun `test increase cart`(){
        viewModel.increaseCart(mockk())
        coVerify { repoCart.increaseCart(any()) }
    }
    @Test
    fun `test set cart notes`(){
        viewModel.updateCartNote(mockk())
        coVerify { repoCart.setCartNotes(any()) }
    }
    @Test
    fun `test delete cart`(){
        viewModel.deleteCart(mockk())
        coVerify { repoCart.deleteCart(any()) }
    }
    @Test
    fun `test delete all cart`(){
        viewModel.deleteAll()
        coVerify { repoCart.deleteAll() }
    }
    @Test
    fun `test order`(){
        viewModel.cartList.getOrAwaitValue()
        viewModel.order()
        coVerify { repoUser.getCurrentUser() }
        coVerify { repoCart.order(any(),any()) }
        val result = viewModel.checkoutResult.getOrAwaitValue()
        Assert.assertEquals(result.payload, true)
    }
}