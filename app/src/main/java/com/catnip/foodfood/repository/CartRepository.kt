package com.catnip.foodfood.repository

import com.catnip.foodfood.api.datasource.ApiDataSource
import com.catnip.foodfood.api.model.order.OrderItemRequest
import com.catnip.foodfood.api.model.order.OrderRequest
import com.catnip.foodfood.local.database.datasource.CartDataSource
import com.catnip.foodfood.local.database.entity.CartEntity
import com.catnip.foodfood.local.database.entity.toCartList
import com.catnip.foodfood.model.Cart
import com.catnip.foodfood.model.Food
import com.catnip.foodfood.model.toCartEntity
import com.catnip.foodfood.utils.ResultWrapper
import com.catnip.foodfood.utils.proceed
import com.catnip.foodfood.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface CartRepository {
    fun getCarts(): Flow<ResultWrapper<Pair<List<Cart>, Int>>>
    suspend fun createCart(food: Food, qty: Int): Flow<ResultWrapper<Boolean>>
    suspend fun decreaseCart(cart: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun increaseCart(cart: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun setCartNotes(cart: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun deleteCart(cart: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun order(carts: List<Cart>, username: String): Flow<ResultWrapper<Boolean>>
    suspend fun deleteAll()
}

class CartRepositoryImpl(
    private val cartDataSource: CartDataSource,
    private val apiDataSource: ApiDataSource
) : CartRepository {
    override fun getCarts(): Flow<ResultWrapper<Pair<List<Cart>, Int>>> {
        return cartDataSource.getAllCarts()
            .map {
                proceed {
                    val result = it.toCartList()
                    val totalPrice = result.sumOf {
                        val pricePerItem = it.foodPrice
                        val quantity = it.quantity
                        pricePerItem * quantity
                    }
                    Pair(result, totalPrice)
                }
            }.map {
                if (it.payload?.first?.isEmpty() == true)
                    ResultWrapper.Empty(it.payload)
                else
                    it
            }
            .onStart {
                emit(ResultWrapper.Loading())
                delay(2000)
            }
    }

    override suspend fun createCart(food: Food, qty: Int): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            val affectedRow = cartDataSource.insertCart(
                CartEntity(
                    foodName = food.nama,
                    foodImage = food.imageUrl,
                    foodPrice = food.harga,
                    quantity = qty
                )
            )
            affectedRow > 0
        }
    }

    override suspend fun decreaseCart(cart: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = cart.copy().apply {
            quantity -= 1
        }
        return if (modifiedCart.quantity <= 0) {
            proceedFlow { cartDataSource.deleteCart(modifiedCart.toCartEntity()) > 0 }
        } else {
            proceedFlow { cartDataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
        }
    }

    override suspend fun increaseCart(cart: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = cart.copy().apply {
            quantity += 1
        }
        return proceedFlow { cartDataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
    }

    override suspend fun setCartNotes(cart: Cart): Flow<ResultWrapper<Boolean>> {
        val x  = proceedFlow {
            cartDataSource.updateCart(cart.toCartEntity()) > 0
        }
        x.collect()
        return x
    }

    override suspend fun deleteCart(cart: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { cartDataSource.deleteCart(cart.toCartEntity()) > 0 }
    }

    override suspend fun order(carts: List<Cart>, username: String): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            val orderItems = carts.map {
                OrderItemRequest(it.foodName, it.quantity, it.notes, it.foodPrice)
            }
            val totalPrice = orderItems.sumOf {
                val pricePerItem = it.harga
                val quantity = it.qty
                pricePerItem * quantity
            }
            val orderRequest = OrderRequest(username,totalPrice,orderItems)
            apiDataSource.createOrder(orderRequest).status
        }
    }

    override suspend fun deleteAll() {
        cartDataSource.deleteAllCarts()
    }
}