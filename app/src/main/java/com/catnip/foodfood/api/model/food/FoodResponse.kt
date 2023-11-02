package com.catnip.foodfood.api.model.food

import androidx.annotation.Keep
import com.catnip.foodfood.model.Food
import com.google.gson.annotations.SerializedName

@Keep
data class FoodResponse(
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("nama")
    val nama: String?,
    @SerializedName("harga_format")
    val hargaFormat: String?,
    @SerializedName("harga")
    val harga: Int?,
    @SerializedName("detail")
    val detail: String?,
    @SerializedName("alamat_resto")
    val alamatResto: String?
)
private fun FoodResponse.toFood() = Food(
    imageUrl = this.imageUrl.orEmpty(),
    nama = this.nama.orEmpty(),
    hargaFormat = this.hargaFormat.orEmpty(),
    harga = this.harga?:0,
    detail = this.detail.orEmpty(),
    alamatResto = this.alamatResto.orEmpty()
)
fun Collection<FoodResponse>.toFoodList() = this.map {
    it.toFood()
}
