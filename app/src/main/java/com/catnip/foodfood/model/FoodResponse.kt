package com.catnip.foodfood.model

data class FoodResponse(
    val status: Boolean,
    val code: Int,
    val message: String,
    val data: List<Food>
)