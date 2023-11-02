package com.catnip.foodfood.api.model.order

data class OrderResponse(
    val status: Boolean,
    val message: String,
    val code: Int
)
