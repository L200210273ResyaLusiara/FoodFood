package com.catnip.foodfood.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.catnip.foodfood.local.database.AppDatabase
import com.catnip.foodfood.local.database.dao.CartDao
import com.catnip.foodfood.local.database.entity.Cart
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CartRepository(application: Application) {
    private val mCartDao: CartDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = AppDatabase.getInstance(application)
        mCartDao = db.cartDao()
    }
    fun getAllCarts(): LiveData<List<Cart>> = mCartDao.getAllCarts()
    fun insert(cart: Cart) {
        executorService.execute { mCartDao.insertCart(cart) }
    }
    fun delete(cart: Cart) {
        executorService.execute { mCartDao.deleteCart(cart) }
    }
    fun deleteAll() {
        executorService.execute { mCartDao.deleteAllCarts() }
    }
    fun update(cart: Cart) {
        executorService.execute { mCartDao.updateCart(cart) }
    }
}