package com.catnip.foodfood.model

data class OrderRequest(
    val username: String,
    val total: Int,
    val orders: List<Order>
)