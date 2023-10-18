package com.catnip.foodfood.presentation.checkout

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.catnip.foodfood.api.RetrofitClient
import com.catnip.foodfood.local.database.entity.Cart
import com.catnip.foodfood.model.OrderRequest
import com.catnip.foodfood.model.OrderResponse
import com.catnip.foodfood.repository.CartRepository
import com.catnip.foodfood.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutViewModel(private val repoCart: CartRepository,private val repoUser: UserRepository) : ViewModel() {
    val cartList = repoCart.getAllCarts()
    fun getCurrentUser() = repoUser.getCurrentUser()
    private val _orderResult = MutableLiveData<OrderResponse>()
    val orderResult: LiveData<OrderResponse>
        get() = _orderResult
    fun decreaseCart(cart: Cart) {
        cart.quantity-=1
        if(cart.quantity==0){
            repoCart.delete(cart)
        }else{
            repoCart.update(cart)
        }
    }
    fun increaseCart(cart: Cart) {
        repoCart.update(cart.apply { quantity+=1 })
    }
    fun deleteCart(cart: Cart){
        repoCart.delete(cart)
    }
    fun deleteAll(){
        repoCart.deleteAll()
    }
    fun updateCartNote(cart: Cart){
        repoCart.update(cart)
    }
    fun order(orderRequest: OrderRequest){
        RetrofitClient.apiInstance
            .order(orderRequest)
            .enqueue(object : Callback<OrderResponse> {
                override fun onResponse(
                    call: Call<OrderResponse>,
                    response: Response<OrderResponse>
                ) {
                    if (response.isSuccessful){
                        _orderResult.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    t.message?.let {
                        Log.d("Failure", it)
                    }
                }
            })
    }
}