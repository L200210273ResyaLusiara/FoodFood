package com.catnip.foodfood.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catnip.foodfood.model.Food
import com.catnip.foodfood.repository.CartRepository
import com.catnip.foodfood.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor (
    private val repoCart: CartRepository,
) : ViewModel() {
    private var food:Food?=null

    private val _quantity = MutableLiveData<Int>().apply {
        postValue(1)
    }
    val quantity: LiveData<Int>
        get() = _quantity

    private val _price = MutableLiveData<Int>().apply {
        postValue(food?.harga)
    }
    val price: LiveData<Int>
        get() = _price

    private val _addToCartResult = MutableLiveData<ResultWrapper<Boolean>>()
    val addToCartResult: LiveData<ResultWrapper<Boolean>>
        get() = _addToCartResult

    fun increment() {
        val newQty = (quantity.value ?: 0)+1
        _quantity.postValue(newQty)
        _price.postValue(((food?.harga)?:0)*newQty)
    }

    fun decrement() {
        if((quantity.value ?: 0)>1){
            val newQty = (quantity.value ?: 0)-1
            _quantity.postValue(newQty)
            _price.postValue(((food?.harga)?:0)*newQty)
        }
    }

    fun addToCart() {
        viewModelScope.launch {
            val productQuantity =
                if ((quantity.value ?: 0) <= 0) 1 else quantity.value ?: 1
            food.let {
                it?.let { f ->
                    repoCart.createCart(f, productQuantity).collect { result ->
                        _addToCartResult.postValue(result)
                    }
                }
            }
        }
    }

    fun setFood(food:Food?){
        this.food=food
        _price.postValue(food?.harga)
    }
}