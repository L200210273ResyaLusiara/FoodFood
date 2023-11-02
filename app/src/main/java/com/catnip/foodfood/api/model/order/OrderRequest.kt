package com.catnip.foodfood.api.model.order

data class OrderRequest(
    val username: String,
    val total: Int,
    val orderItemRequests: List<OrderItemRequest>
)