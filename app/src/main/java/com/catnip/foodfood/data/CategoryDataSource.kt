package com.catnip.foodfood.data

import com.catnip.foodfood.R
import com.catnip.foodfood.model.Category


interface CategoryDataSource {
    fun getCategories(): List<Category>
}

class CategoryDataSourceImpl() : CategoryDataSource {
    override fun getCategories(): List<Category> = listOf(
        Category(
            name = "Burger",
            categoryImg = R.drawable.img_burger1
        ),
        Category(
            name = "Mie",
            categoryImg = R.drawable.img_mie2
        ),
        Category(
            name = "Snack",
            categoryImg = R.drawable.img_stick1
        ),
        Category(
            name = "Makanan",
            categoryImg = R.drawable.img_makanan
        ),
    )

}