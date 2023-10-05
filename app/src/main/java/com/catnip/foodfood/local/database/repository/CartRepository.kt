package com.catnip.foodfood.local.database.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.catnip.foodfood.local.database.AppDatabase
import com.catnip.foodfood.local.database.dao.CartDao
import com.catnip.foodfood.local.database.entity.Cart
import com.catnip.foodfood.local.database.relation.CartFoodRelation
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CartRepository(application: Application) {
    private val mCartDao: CartDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = AppDatabase.getInstance(application)
        mCartDao = db.cartDao()
    }
    fun getAllCarts(): LiveData<List<CartFoodRelation>> = mCartDao.getAllCarts()
    fun insert(cart: Cart) {
        executorService.execute { mCartDao.insertCart(cart) }
    }
    fun delete(cart: Cart) {
        executorService.execute { mCartDao.deleteCart(cart) }
    }
    fun update(cart: Cart) {
        executorService.execute { mCartDao.updateCart(cart) }
    }
}