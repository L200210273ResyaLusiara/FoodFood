package com.catnip.foodfood.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.catnip.foodfood.local.database.entity.Cart

@Dao
interface CartDao {

    @Query("SELECT * FROM CARTS")
    fun getAllCarts(): LiveData<List<Cart>>

    @Query("SELECT * FROM CARTS WHERE id == :id")
    fun getCartById(id: Int): Cart

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCart(cart: Cart): Long

    @Delete
    fun deleteCart(cart: Cart): Int

    @Query("DELETE FROM CARTS")
    fun deleteAllCarts(): Int

    @Update
    fun updateCart(cart: Cart): Int
}