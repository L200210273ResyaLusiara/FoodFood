package com.catnip.foodfood.local.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.catnip.foodfood.local.database.entity.Cart
import com.catnip.foodfood.local.database.entity.Food

data class CartFoodRelation(
    @Embedded
    val cart: Cart,
    @Relation(parentColumn = "food_id", entityColumn = "id")
    val food: Food
)