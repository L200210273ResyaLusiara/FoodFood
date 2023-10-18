package com.catnip.foodfood.presentation.fragmentprofile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import coil.load
import coil.transform.CircleCropTransformation
import com.catnip.foodfood.R
import com.catnip.foodfood.data.FirebaseAuthDataSourceImpl
import com.catnip.foodfood.databinding.FragmentProfileBinding
import com.catnip.foodfood.presentation.editprofile.EditProfileActivity
import com.catnip.foodfood.presentation.login.LoginActivity
import com.catnip.foodfood.repository.UserRepositoryImpl
import com.catnip.foodfood.utils.GenericViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    private val binding: FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }
    private fun createViewModel(): ProfileViewModel {
        val firebaseAuth = FirebaseAuth.getInstance()
        val dataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val repo = UserRepositoryImpl(dataSource)
        return ProfileViewModel(repo)
    }

    private val viewModel: ProfileViewModel by viewModels {
        GenericViewModelFactory.create(createViewModel())
    }

    override fun onStart() {
        super.onStart()
        setupProfile()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
    }

    private fun setClickListeners() {
        with(binding){
            btnLogout.setOnClickListener{doLogout()}
            ivEdit.setOnClickListener{startActivity(Intent(requireContext(),EditProfileActivity::class.java))}
        }
    }

    private fun doLogout() {
        val dialog = AlertDialog.Builder(requireContext()).setMessage("Do you want to logout ?")
            .setPositiveButton(
                "Yes"
            ) { dialog, id ->
                viewModel.doLogout()
                navigateToLogin()
            }
            .setNegativeButton(
                "No"
            ) { dialog, id ->
                //no-op , do nothing
            }.create()
        dialog.show()
    }

    private fun navigateToLogin() {
        startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    private fun setupProfile() {
        viewModel.getCurrentUser()?.let {
            binding.etUsername.setText(it.username)
            binding.etEmail.setText(it.email)
            binding.ivProfile.load(it.photoUrl) {
                crossfade(true)
                placeholder(R.drawable.profile)
                error(R.drawable.profile)
                transformations(CircleCropTransformation())
            }
        }
    }
}