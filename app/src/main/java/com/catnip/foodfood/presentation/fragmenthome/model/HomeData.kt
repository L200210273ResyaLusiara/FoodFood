package com.catnip.foodfood.presentation.fragmenthome.model

import com.catnip.foodfood.model.Category
import com.catnip.foodfood.model.Food
import com.catnip.foodfood.utils.ResultWrapper

data class HomeData (
    val foods : ResultWrapper<List<Food>>,
    val categories : ResultWrapper<List<Category>>
)