package com.catnip.foodfood.presentation.fragmenthome

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.catnip.foodfood.model.Food
import com.catnip.foodfood.repository.FoodRepository
import com.catnip.foodfood.utils.MainCoroutineRule
import com.catnip.foodfood.utils.ResultWrapper
import com.catnip.foodfood.utils.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.Locale.Category

class HomeViewModelTest{
    @MockK
    private lateinit var repo:FoodRepository

    private lateinit var viewModel:HomeViewModel

    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule : TestRule = MainCoroutineRule(UnconfinedTestDispatcher())

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        viewModel = spyk(
            HomeViewModel(repo),
            recordPrivateCalls = true
        )
        coEvery { repo.getCategories() } returns flow{
            emit(ResultWrapper.Success(listOf(
                mockk(relaxed = true),
                mockk(relaxed = true),
                mockk(relaxed = true)
            )))
        }
        coEvery { repo.getProducts() } returns flow{
            emit(ResultWrapper.Success(listOf(
                mockk(relaxed = true),
                mockk(relaxed = true),
                mockk(relaxed = true),
                mockk(relaxed = true)
            )))
        }
    }
    @Test
    fun `home data set`(){
        val result = viewModel.homeData.getOrAwaitValue()
        assertEquals(result.foods.payload?.size,4)
        assertEquals(result.categories.payload?.size,3)
        verify {
            viewModel invoke "mapToHomeData" withArguments listOf(
                any<ResultWrapper<List<Category>>>(),
                any<ResultWrapper<List<Food>>>(),
            )
        }
        verify { viewModel.setSelectedCategory() }
    }
}