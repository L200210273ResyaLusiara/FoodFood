package com.catnip.foodfood.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.catnip.foodfood.local.database.entity.Food

@Dao
interface FoodDao {

    @Query("SELECT * FROM foods")
    fun getAllFoods(): List<Food>

    @Query("SELECT * FROM foods WHERE id == :id")
    fun getFoodById(id: Int): Food

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFoods(foods: List<Food>)
}