package com.catnip.foodfood.model
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Food(
    @SerializedName("image_url")
    val imageUrl: String,
    val nama: String,
    @SerializedName("harga_format")
    val hargaFormat: String,
    val harga: Int,
    val detail: String,
    @SerializedName("alamat_resto")
    val alamatResto: String
):Parcelable