package com.catnip.foodfood.model

data class CategoryResponse(
    val status: Boolean,
    val code: Int,
    val message: String,
    val data: List<Category>
)