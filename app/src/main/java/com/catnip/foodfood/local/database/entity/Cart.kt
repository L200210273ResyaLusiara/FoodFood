package com.catnip.foodfood.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carts")
data class Cart(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "food_id")
    var foodId : Int = 0,
    @ColumnInfo(name = "quantity")
    var quantity: Int = 0,
    @ColumnInfo(name = "notes")
    var notes: String? = null,
)