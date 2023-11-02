package com.catnip.foodfood.api.model.order

data class OrderItemRequest(
    val nama: String?,
    val qty: Int,
    val catatan: String?,
    val harga: Int
)