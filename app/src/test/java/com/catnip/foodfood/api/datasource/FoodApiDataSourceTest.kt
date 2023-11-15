package com.catnip.foodfood.api.datasource

import com.catnip.foodfood.api.model.category.CategoriesResponse
import com.catnip.foodfood.api.model.food.FoodsResponse
import com.catnip.foodfood.api.model.order.OrderRequest
import com.catnip.foodfood.api.model.order.OrderResponse
import com.catnip.foodfood.api.service.ApiService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class FoodApiDataSourceTest{
    @MockK
    lateinit var service: ApiService
    private lateinit var dataSource:ApiDataSource
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = FoodApiDataSource(service)
    }
    @Test
    fun getFoods() {
        runTest {
            val mockResponse = mockk<FoodsResponse>()
            coEvery { service.getFoods(any()) } returns mockResponse
            val response = dataSource.getFoods("mie")
            coVerify { service.getFoods(any()) }
            assertEquals(response, mockResponse)
        }
    }
    @Test
    fun getCategories() {
        runTest {
            val mockResponse = mockk<CategoriesResponse>(relaxed = true)
            coEvery { service.getCategories() } returns mockResponse
            val response = dataSource.getCategories()
            coVerify { service.getCategories() }
            assertEquals(response, mockResponse)
        }
    }
    @Test
    fun createOrder() {
        runTest {
            val mockResponse = mockk<OrderResponse>(relaxed = true)
            val mockRequest = mockk<OrderRequest>(relaxed = true)
            coEvery { service.order(any()) } returns mockResponse
            val response = dataSource.createOrder(mockRequest)
            coVerify { service.order(any()) }
            assertEquals(response, mockResponse)
        }
    }
}