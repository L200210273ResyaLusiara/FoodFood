package com.catnip.foodfood.api.model.food

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class FoodsResponse(
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("code")
    val code: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: List<FoodResponse>?
)