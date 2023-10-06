package com.catnip.foodfood.model

import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(),
    val img: Int,
    val username: String,
    val password: String,
    val email: String,
    val phone: String
)
