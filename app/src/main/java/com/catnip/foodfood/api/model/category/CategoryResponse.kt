package com.catnip.foodfood.api.model.category

import androidx.annotation.Keep
import com.catnip.foodfood.model.Category
import com.google.gson.annotations.SerializedName

@Keep
data class CategoryResponse(
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("nama")
    val nama: String?
)
private fun CategoryResponse.toCategory() = Category(
    imageUrl = this.imageUrl.orEmpty(),
    nama = this.nama.orEmpty()
)
fun Collection<CategoryResponse>.toCategoryList() = this.map {
    it.toCategory()
}
