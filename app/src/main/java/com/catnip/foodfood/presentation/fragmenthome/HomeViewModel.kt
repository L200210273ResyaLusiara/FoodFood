package com.catnip.foodfood.presentation.fragmenthome

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.catnip.foodfood.api.RetrofitClient
import com.catnip.foodfood.local.database.AppDatabase
import com.catnip.foodfood.model.Category
import com.catnip.foodfood.model.CategoryResponse
import com.catnip.foodfood.model.Food
import com.catnip.foodfood.model.FoodResponse
import com.catnip.foodfood.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val foods = MutableLiveData<List<Food>>()
    private val categories = MutableLiveData<List<Category>>()
    fun setFoods() {
        RetrofitClient.apiInstance
            .getFoods()
            .enqueue(object : Callback<FoodResponse> {
                override fun onResponse(
                    call: Call<FoodResponse>,
                    response: Response<FoodResponse>
                ) {
                    if (response.isSuccessful){
                        foods.postValue(response.body()?.data)
                    }
                }
                override fun onFailure(call: Call<FoodResponse>, t: Throwable) {
                    t.message?.let { Log.d("Failure", it) }
                }
            })

    }

    fun getFoods(): LiveData<List<Food>>{
        return foods
    }

    fun setCategories() {
        RetrofitClient.apiInstance
            .getCategories()
            .enqueue(object : Callback<CategoryResponse> {
                override fun onResponse(
                    call: Call<CategoryResponse>,
                    response: Response<CategoryResponse>
                ) {
                    if (response.isSuccessful){
                        categories.postValue(response.body()?.data)
                    }
                }
                override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                    t.message?.let { Log.d("Failure", it) }
                }
            })

    }

    fun getCategories(): LiveData<List<Category>>{
        return categories
    }
}