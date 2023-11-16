package com.catnip.foodfood.presentation.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.catnip.foodfood.model.Food
import com.catnip.foodfood.presentation.fragmentcart.CartViewModel
import com.catnip.foodfood.repository.CartRepository
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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class DetailViewModelTest{
    @MockK
    private lateinit var repo: CartRepository
    private lateinit var viewModel: DetailViewModel

    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule : TestRule = MainCoroutineRule(UnconfinedTestDispatcher())

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        val updateResultMock = flow {
            emit(ResultWrapper.Success(true))
        }
        coEvery { repo.createCart(any(),any())} returns updateResultMock
        viewModel= spyk(DetailViewModel(repo))
    }

    @Test
    fun `test increment`(){
        viewModel.increment()
        val result = viewModel.quantity.getOrAwaitValue()
        assertEquals(result,2)
    }

    @Test
    fun `test decrement with value 1`(){
        viewModel.decrement()
        val result = viewModel.quantity.getOrAwaitValue()
        assertEquals(result,1)
    }

    @Test
    fun `test decrement with more than 1`(){
        viewModel.increment()
        viewModel.increment()
        viewModel.decrement()
        val result = viewModel.quantity.getOrAwaitValue()
        assertEquals(result,2)
    }

    @Test
    fun `test setFood and price`(){
        viewModel.setFood(Food("-","-","-",5000,"-","-"))
        val result = viewModel.price.getOrAwaitValue()
        assertEquals(result,5000)
    }

    @Test
    fun `test add to cart`(){
        viewModel.setFood(Food("-","-","-",5000,"-","-"))
        viewModel.addToCart()
        coVerify { repo.createCart(any(),any()) }
        val result = viewModel.addToCartResult.getOrAwaitValue()
        assertEquals(result.payload, true)
    }
}