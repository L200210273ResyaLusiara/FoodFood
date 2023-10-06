package com.catnip.foodfood.presentation.fragmentprofile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import coil.load
import com.catnip.foodfood.data.UserDataSource
import com.catnip.foodfood.data.UserDataSourceImpl
import com.catnip.foodfood.databinding.FragmentCartBinding
import com.catnip.foodfood.databinding.FragmentProfileBinding
import com.catnip.foodfood.local.database.entity.Cart
import com.catnip.foodfood.local.database.repository.CartRepository
import com.catnip.foodfood.model.User
import com.catnip.foodfood.presentation.checkout.CheckoutActivity
import com.catnip.foodfood.presentation.checkout.CheckoutViewModel
import com.catnip.foodfood.presentation.fragmentcart.adapter.CartAdapter
import com.catnip.foodfood.presentation.fragmentcart.adapter.CartListener
import com.catnip.foodfood.utils.GenericViewModelFactory
import java.text.NumberFormat
import java.util.Locale

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProfile()
    }

    private fun setupProfile() {
        val user = UserDataSourceImpl().getUser()
        binding.ivProfile.load(user.img){
            crossfade(true)
        }
        binding.etUsername.setText(user.username)
        binding.etEmail.setText(user.email)
        binding.etPassword.setText(user.password)
        binding.etPhone.setText(user.phone)
    }
}