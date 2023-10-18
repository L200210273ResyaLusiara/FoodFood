package com.catnip.foodfood.presentation.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catnip.foodfood.repository.UserRepository
import com.catnip.foodfood.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    private val _registerResult = MutableLiveData<ResultWrapper<Boolean>>()
    val registerResult: LiveData<ResultWrapper<Boolean>>
        get() = _registerResult

    fun doRegister(username: String, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.doRegister(username, email, password).collect {
                _registerResult.postValue(it)
            }
        }
    }
}