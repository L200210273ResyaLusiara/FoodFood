package com.catnip.foodfood.model

import com.catnip.foodfood.local.database.entity.CartEntity

data class Cart(
    var id: Int? = null,
    var foodName : String? = null,
    var foodImage : String? = null,
    var foodPrice : Int = 0,
    var quantity: Int = 0,
    var notes: String? = null,
)

fun Cart?.toCartEntity() = CartEntity(
    id = this?.id ?: 0,
    foodName = this?.foodName.orEmpty(),
    foodImage = this?.foodImage.orEmpty(),
    foodPrice = this?.foodPrice ?: 0,
    quantity = this?.quantity ?: 0,
    notes = this?.notes.orEmpty()
)