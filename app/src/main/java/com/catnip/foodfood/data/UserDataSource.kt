package com.catnip.foodfood.data

import com.catnip.foodfood.R
import com.catnip.foodfood.model.User

interface UserDataSource {
    fun getUser(): User
}

class UserDataSourceImpl() : UserDataSource {
    override fun getUser(): User =
        User(
            img = R.drawable.profile,
            username = "Resya",
            password = "Resya123",
            email = "l200210273@student.ums.ac.id",
            phone = "081321216104"
        )
}