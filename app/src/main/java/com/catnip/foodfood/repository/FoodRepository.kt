package com.catnip.foodfood.repository

import com.catnip.foodfood.api.datasource.ApiDataSource
import com.catnip.foodfood.api.model.category.toCategoryList
import com.catnip.foodfood.api.model.food.toFoodList
import com.catnip.foodfood.model.Category
import com.catnip.foodfood.model.Food
import com.catnip.foodfood.utils.ResultWrapper
import com.catnip.foodfood.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface FoodRepository {
    fun getCategories(): Flow<ResultWrapper<List<Category>>>
    fun getProducts(category: String? = null): Flow<ResultWrapper<List<Food>>>
}

class FoodRepositoryImpl(
    private val apiDataSource: ApiDataSource
) : FoodRepository {
    override fun getCategories(): Flow<ResultWrapper<List<Category>>> {
        return proceedFlow {
            apiDataSource.getCategories().data?.toCategoryList() ?: emptyList()
        }
    }
    override fun getProducts(category: String?): Flow<ResultWrapper<List<Food>>> {
        return proceedFlow {
            apiDataSource.getFoods(category).data?.toFoodList() ?: emptyList()
        }
    }
}
