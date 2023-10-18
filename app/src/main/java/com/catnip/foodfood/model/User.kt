package com.catnip.foodfood.model

import com.google.firebase.auth.FirebaseUser

data class User(
    val username: String,
    val photoUrl: String,
    val email: String,
)

fun FirebaseUser?.toUser(): User? = if (this != null) User(
    username = this.displayName.orEmpty(),
    photoUrl = this.photoUrl.toString(),
    email = this.email.orEmpty(),
) else null