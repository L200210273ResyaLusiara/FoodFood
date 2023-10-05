package com.catnip.foodfood.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.catnip.foodfood.local.database.entity.Cart
import com.catnip.foodfood.local.database.relation.CartFoodRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("SELECT * FROM CARTS")
    fun getAllCarts(): List<CartFoodRelation>

    @Query("SELECT * FROM CARTS WHERE id == :id")
    fun getCartById(id: Int): CartFoodRelation

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCart(cart: Cart): Long

    @Delete
    fun deleteCart(cart: Cart): Int

    @Update
    fun updateCart(cart: Cart): Int
}