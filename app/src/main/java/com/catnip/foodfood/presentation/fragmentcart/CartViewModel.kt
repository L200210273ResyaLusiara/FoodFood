package com.catnip.foodfood.presentation.fragmentcart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.catnip.foodfood.model.Cart
import com.catnip.foodfood.repository.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel(private val repoCart: CartRepository) : ViewModel() {

    val cartList = repoCart.getCarts().asLiveData(Dispatchers.IO)

    fun decreaseCart(cart: Cart) {
        viewModelScope.launch {
            repoCart.decreaseCart(cart).collect {
                Log.d("CheckoutViewModel", " : Increase Cart -> $it ${it.payload} ${it.exception}")
            }
        }
    }

    fun increaseCart(cart: Cart) {
        viewModelScope.launch {
            repoCart.increaseCart(cart).collect {
                Log.d("CheckoutViewModel", " : Increase Cart -> $it ${it.payload} ${it.exception}")
            }
        }
    }

    fun deleteCart(cart: Cart){
        viewModelScope.launch {
            repoCart.deleteCart(cart).collect {
                Log.d("CheckoutViewModel", " : Remove Cart -> $it ${it.payload} ${it.exception}")
            }
        }
    }

    fun updateCartNote(cart: Cart){
        viewModelScope.launch {
            repoCart.setCartNotes(cart)
        }
    }
}