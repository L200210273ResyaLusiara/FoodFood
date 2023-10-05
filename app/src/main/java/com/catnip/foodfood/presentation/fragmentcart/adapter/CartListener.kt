package com.catnip.foodfood.presentation.fragmentcart.adapter

import com.catnip.foodfood.local.database.entity.Cart


interface CartListener {
    fun onPlusTotalItemCartClicked(cart: Cart)
    fun onMinusTotalItemCartClicked(cart: Cart)
    fun onRemoveCartClicked(cart: Cart)
    fun onUserDoneEditingNotes(cart: Cart)
}