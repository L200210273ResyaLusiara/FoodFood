package com.catnip.foodfood.presentation.fragmentcart

import androidx.lifecycle.ViewModel
import com.catnip.foodfood.local.database.entity.Cart
import com.catnip.foodfood.local.database.repository.CartRepository

class CartViewModel(private val repo: CartRepository) : ViewModel() {

    val cartList = repo.getAllCarts()

    fun decreaseCart(cart: Cart) {
        cart.quantity-=1
        if(cart.quantity==0){
            repo.delete(cart)
        }else{
            repo.update(cart)
        }
    }
    fun increaseCart(cart: Cart) {
        repo.update(cart.apply { quantity+=1 })
    }
    fun deleteCart(cart: Cart){
        repo.delete(cart)
    }
    fun updateCartNote(cart: Cart){
        repo.update(cart)
    }
}