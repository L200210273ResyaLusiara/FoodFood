package com.catnip.foodfood.presentation.checkout

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.catnip.foodfood.model.Cart
import com.catnip.foodfood.repository.CartRepository
import com.catnip.foodfood.repository.UserRepository
import com.catnip.foodfood.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(private val repoCart: CartRepository, repoUser: UserRepository) : ViewModel() {
    val cartList = repoCart.getCarts().asLiveData(Dispatchers.IO)
    val currentUser = repoUser.getCurrentUser()

    private val _checkoutResult = MutableLiveData<ResultWrapper<Boolean>>()
    val checkoutResult: LiveData<ResultWrapper<Boolean>>
        get() = _checkoutResult
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

    fun order() {
        viewModelScope.launch(Dispatchers.IO) {
            val carts = cartList.value?.payload?.first ?: return@launch
            currentUser?.let {user->
                repoCart.order(carts, user.username).collect {
                    _checkoutResult.postValue(it)
                }
            }
        }
    }

    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            repoCart.deleteAll()
        }
    }
}