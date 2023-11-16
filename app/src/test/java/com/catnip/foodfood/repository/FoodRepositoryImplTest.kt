package com.catnip.foodfood.repository

import app.cash.turbine.test
import com.catnip.foodfood.api.datasource.ApiDataSource
import com.catnip.foodfood.api.model.category.CategoriesResponse
import com.catnip.foodfood.api.model.category.CategoryResponse
import com.catnip.foodfood.api.model.food.FoodResponse
import com.catnip.foodfood.api.model.food.FoodsResponse
import com.catnip.foodfood.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.IllegalStateException

class FoodRepositoryImplTest{
    @MockK
    lateinit var remoteDataSource: ApiDataSource

    private lateinit var repository: FoodRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = FoodRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `get categories, with result loading`() {
        val mockCategoryResponse = mockk<CategoriesResponse>()
        runTest {
            coEvery { remoteDataSource.getCategories() } returns mockCategoryResponse
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { remoteDataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get categories, with result success`() {
        val fakeCategoryResponse = CategoryResponse(
            imageUrl = "url",
            nama = "name"
        )
        val fakeCategoriesResponse = CategoriesResponse(
            code = 200,
            status = true,
            message = "Success",
            data = listOf(fakeCategoryResponse)
        )
        runTest {
            coEvery { remoteDataSource.getCategories() } returns fakeCategoriesResponse
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.size, 1)
                assertEquals(data.payload?.get(0)?.nama, "name")
                coVerify { remoteDataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get categories, with result empty`() {
        val fakeCategoriesResponse = CategoriesResponse(
            code = 200,
            status = true,
            message = "Success but empty",
            data = emptyList()
        )
        runTest {
            coEvery { remoteDataSource.getCategories() } returns fakeCategoriesResponse
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { remoteDataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get categories, with result error`() {
        runTest {
            coEvery { remoteDataSource.getCategories() } throws IllegalStateException("Mock error")
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { remoteDataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get Foods, with result loading`() {
        val mockFoodResponse = mockk<FoodsResponse>()
        runTest {
            coEvery { remoteDataSource.getFoods(any()) } returns mockFoodResponse
            repository.getProducts("mie").map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { remoteDataSource.getFoods(any()) }
            }
        }
    }

    @Test
    fun `get Foods, with result success`() {
        val fakeFoodItemResponse = FoodResponse(
            imageUrl = "url",
            nama = "name",
            hargaFormat = "Rp. 12000",
            harga = 12000,
            detail = "a",
            alamatResto = "b"
        )
        val fakeFoodsResponse = FoodsResponse(
            code = 200,
            status = true,
            message = "Success",
            data = listOf(fakeFoodItemResponse)
        )
        runTest {
            coEvery { remoteDataSource.getFoods(any()) } returns fakeFoodsResponse
            repository.getProducts("mie").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.size, 1)
                assertEquals(data.payload?.get(0)?.nama, "name")
                coVerify { remoteDataSource.getFoods(any()) }
            }
        }
    }

    @Test
    fun `get Foods, with result empty`() {
        val fakeFoodsResponse = FoodsResponse(
            code = 200,
            status = true,
            message = "Success",
            data = emptyList()
        )
        runTest {
            coEvery { remoteDataSource.getFoods(any()) } returns fakeFoodsResponse
            repository.getProducts().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { remoteDataSource.getFoods(any()) }
            }
        }
    }

    @Test
    fun `get Foods, with result error`() {
        runTest {
            coEvery { remoteDataSource.getFoods(any()) } throws IllegalStateException("Mock error")
            repository.getProducts("mie").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { remoteDataSource.getFoods(any()) }
            }
        }
    }
}