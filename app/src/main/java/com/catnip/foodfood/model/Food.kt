package com.catnip.foodfood.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Food(

    val name: String,
    val price: String,
    val image: String,
    val desc: String
) : Parcelable