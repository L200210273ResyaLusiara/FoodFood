package com.catnip.foodfood.model
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Food(
    val imageUrl: String,
    val nama: String,
    val hargaFormat: String,
    val harga: Int,
    val detail: String,
    val alamatResto: String
):Parcelable