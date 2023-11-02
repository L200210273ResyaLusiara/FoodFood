package com.catnip.foodfood.presentation.fragmenthome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.catnip.foodfood.model.Category
import com.catnip.foodfood.model.Food
import com.catnip.foodfood.presentation.fragmenthome.model.HomeData
import com.catnip.foodfood.repository.FoodRepository
import com.catnip.foodfood.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: FoodRepository) : ViewModel() {
    private val foodsFlow =
        MutableStateFlow<ResultWrapper<List<Food>>>(ResultWrapper.Loading())

    val homeData: LiveData<HomeData>
        get() = repo.getCategories().combine(foodsFlow) { f1, f2 -> Pair(f1, f2) }
            .map { (categories, products) ->
                mapToHomeData(categories, products)
            }.onStart {
                setSelectedCategory(null)
            }.asLiveData(Dispatchers.IO)

    private fun mapToHomeData(
        categoryResult: ResultWrapper<List<Category>>,
        foodResult: ResultWrapper<List<Food>>
    ): HomeData =
        HomeData(
            categories = categoryResult,
            foods = foodResult
        )

    fun setSelectedCategory(category: String? = null) {
        viewModelScope.launch {
            repo.getProducts(if (category == "all") null else category).collect {
                foodsFlow.emit(it)
            }
        }
    }
}