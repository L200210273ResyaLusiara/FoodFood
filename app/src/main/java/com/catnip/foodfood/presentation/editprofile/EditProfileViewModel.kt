package com.catnip.foodfood.presentation.editprofile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catnip.foodfood.repository.UserRepository
import com.catnip.foodfood.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditProfileViewModel(private val repo: UserRepository) : ViewModel() {
    private val _changeProfileResult = MutableLiveData<ResultWrapper<Boolean>>()
    val changeProfileResult: LiveData<ResultWrapper<Boolean>>
        get() = _changeProfileResult

    fun getCurrentUser() = repo.getCurrentUser()

    fun updateProfile(photoUri: Uri?, username: String?, password: String?, email: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateProfile(photoUri = photoUri, username = username, password = password, email = email).collect {
                _changeProfileResult.postValue(it)
            }
        }
    }
}