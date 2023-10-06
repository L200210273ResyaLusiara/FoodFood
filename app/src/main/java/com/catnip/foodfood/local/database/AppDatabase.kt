package com.catnip.foodfood.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.catnip.foodfood.data.FoodDataSourceImpl
import com.catnip.foodfood.local.database.dao.CartDao
import com.catnip.foodfood.local.database.dao.FoodDao
import com.catnip.foodfood.local.database.AppDatabase.Companion.getInstance
import com.catnip.foodfood.local.database.entity.Cart
import com.catnip.foodfood.local.database.entity.Food
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Database(
    entities = [Cart::class, Food::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun foodDao(): FoodDao
    companion object {
        private const val DB_NAME = "FoodFood.db"
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}