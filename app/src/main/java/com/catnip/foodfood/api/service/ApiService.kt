package com.catnip.foodfood.api.service

import com.catnip.foodfood.api.model.category.CategoriesResponse
import com.catnip.foodfood.api.model.food.FoodsResponse
import com.catnip.foodfood.api.model.order.OrderRequest
import com.catnip.foodfood.api.model.order.OrderResponse
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiService {
    @GET("listmenu")
    suspend fun getFoods(
        @Query("c") category: String? = null
    ): FoodsResponse

    @GET("category")
    suspend fun getCategories(
    ): CategoriesResponse

    @POST("order")
    suspend fun order(
        @Body orderRequest: OrderRequest
    ): OrderResponse

    companion object {
        @JvmStatic
        operator fun invoke(chucker: ChuckerInterceptor): ApiService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(chucker)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://55b8df8a-4893-4583-8a7c-d5ff55d753fc.mock.pstmn.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}