package com.catnip.foodfood.api.datasource

import com.catnip.foodfood.api.model.category.CategoriesResponse
import com.catnip.foodfood.api.model.food.FoodsResponse
import com.catnip.foodfood.api.service.ApiService
import com.catnip.foodfood.api.model.order.OrderRequest
import com.catnip.foodfood.api.model.order.OrderResponse

interface ApiDataSource {
    suspend fun getFoods(category: String? = null): FoodsResponse
    suspend fun getCategories(): CategoriesResponse
    suspend fun createOrder(orderRequest: OrderRequest): OrderResponse
}

class FoodApiDataSource(private val service: ApiService) : ApiDataSource {
    override suspend fun getFoods(category: String?): FoodsResponse {
        return service.getFoods(category)
    }
    override suspend fun getCategories(): CategoriesResponse {
        return service.getCategories()
    }
    override suspend fun createOrder(orderRequest: OrderRequest): OrderResponse {
        return service.order(orderRequest)
    }
}
