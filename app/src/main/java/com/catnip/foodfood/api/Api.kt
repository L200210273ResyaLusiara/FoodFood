package com.catnip.foodfood.api

import com.catnip.foodfood.model.CategoryResponse
import com.catnip.foodfood.model.FoodResponse
import com.catnip.foodfood.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("listmenu")
    fun getFoods(
    ): Call<FoodResponse>

    @GET("category")
    fun getCategories(
    ): Call<CategoryResponse>
}