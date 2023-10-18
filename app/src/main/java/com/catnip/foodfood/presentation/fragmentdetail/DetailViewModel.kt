package com.catnip.foodfood.presentation.fragmentdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.catnip.foodfood.local.database.dao.CartDao
import com.catnip.foodfood.local.database.entity.Cart
import com.catnip.foodfood.repository.CartRepository
import com.catnip.foodfood.model.Food

class DetailViewModel(
    val food: Food,
    private val repo: CartRepository,
) : ViewModel() {
    val quantity = MutableLiveData<Int>().apply {
        postValue(1)
    }
    val price = MutableLiveData<Int>().apply {
        postValue(food.harga)
    }
    fun increment() {
        val newQty = (quantity.value ?: 0)+1
        quantity.postValue(newQty)
        price.postValue(food.harga*newQty)
    }
    fun decrement() {
        if((quantity.value ?: 0)>1){
            val newQty = (quantity.value ?: 0)-1
            quantity.postValue(newQty)
            price.postValue(food.harga*newQty)
        }
    }
    fun addToCart(){
        repo.insert(Cart(foodName = food.nama, foodPrice = food.harga, foodImage = food.imageUrl ,quantity =(quantity.value ?: 0)))
        quantity.postValue(1)
        price.postValue(food.harga)
    }
}