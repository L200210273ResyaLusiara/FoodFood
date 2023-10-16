package com.catnip.foodfood.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("image_url")
    val imageUrl: String,
    val nama: String
)
