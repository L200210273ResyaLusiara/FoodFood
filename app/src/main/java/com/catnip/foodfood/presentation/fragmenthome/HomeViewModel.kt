package com.catnip.foodfood.presentation.fragmenthome

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.catnip.foodfood.local.database.AppDatabase
import com.catnip.foodfood.local.database.entity.Food

class HomeViewModel : ViewModel() {
    val foods = MutableLiveData<List<Food>>()

    fun setFoods(context:Context) {
        foods.postValue(AppDatabase.getInstance(context).foodDao().getAllFoods())
    }

    fun getFoods(): LiveData<List<Food>>{
        return foods
    }
}