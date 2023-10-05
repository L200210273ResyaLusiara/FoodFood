package com.catnip.foodfood.presentation.fragmentdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.catnip.foodfood.local.database.dao.CartDao
import com.catnip.foodfood.local.database.entity.Cart
import com.catnip.foodfood.local.database.entity.Food

class DetailViewModel(
    val food: Food,
    private val cartDao: CartDao,
) : ViewModel() {
    val quantity = MutableLiveData<Int>().apply {
        postValue(1)
    }
    val price = MutableLiveData<Int>().apply {
        postValue(food.price)
    }
    fun increment() {
        val newQty = (quantity.value ?: 0)+1
        quantity.postValue(newQty)
        price.postValue(food.price*newQty)
    }
    fun decrement() {
        if((quantity.value ?: 0)>1){
            val newQty = (quantity.value ?: 0)-1
            quantity.postValue(newQty)
            price.postValue(food.price*newQty)
        }
    }
    fun addToCart(){
        cartDao.insertCart(Cart(foodId = food.id!!, quantity =(quantity.value ?: 0)))
        quantity.postValue(1)
        price.postValue(food.price)
    }
}