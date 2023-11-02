package com.catnip.foodfood.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.catnip.foodfood.model.Cart

@Entity(tableName = "carts")
data class CartEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "food_name")
    var foodName : String? = null,
    @ColumnInfo(name = "food_image")
    var foodImage : String? = null,
    @ColumnInfo(name = "food_price")
    var foodPrice : Int = 0,
    @ColumnInfo(name = "quantity")
    var quantity: Int = 0,
    @ColumnInfo(name = "notes")
    var notes: String? = null,
)

fun CartEntity?.toCart() = Cart(
    id = this?.id ?: 0,
    foodName = this?.foodName.orEmpty(),
    foodImage = this?.foodImage.orEmpty(),
    foodPrice = this?.foodPrice ?: 0,
    quantity = this?.quantity ?: 0,
    notes = this?.notes.orEmpty()
)

fun List<CartEntity?>.toCartList() = this.map { it.toCart() }