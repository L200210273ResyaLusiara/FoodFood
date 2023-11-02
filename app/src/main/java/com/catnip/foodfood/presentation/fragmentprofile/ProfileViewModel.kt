package com.catnip.foodfood.presentation.fragmentprofile

import androidx.lifecycle.ViewModel
import com.catnip.foodfood.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor (private val repo: UserRepository) : ViewModel() {
    fun getCurrentUser() = repo.getCurrentUser()
    fun doLogout() {
        repo.doLogout()
    }
}