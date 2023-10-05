package com.catnip.foodfood.model

import java.util.UUID

data class Category(
    val id: String = UUID.randomUUID().toString(),
    val categoryImg: Int,
    val name: String
)
