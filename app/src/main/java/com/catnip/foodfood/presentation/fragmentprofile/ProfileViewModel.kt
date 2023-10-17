package com.catnip.foodfood.presentation.fragmentprofile

import androidx.lifecycle.ViewModel
import com.catnip.foodfood.repository.UserRepository

class ProfileViewModel(private val repo: UserRepository) : ViewModel() {
    fun getCurrentUser() = repo.getCurrentUser()
    fun doLogout() {
        repo.doLogout()
    }
}