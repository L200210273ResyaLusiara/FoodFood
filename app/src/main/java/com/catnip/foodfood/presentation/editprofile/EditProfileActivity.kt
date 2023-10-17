package com.catnip.foodfood.presentation.editprofile

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import coil.load
import coil.transform.CircleCropTransformation
import com.catnip.foodfood.R
import com.catnip.foodfood.data.FirebaseAuthDataSourceImpl
import com.catnip.foodfood.databinding.ActivityEditProfileBinding
import com.catnip.foodfood.databinding.ActivityMainBinding
import com.catnip.foodfood.repository.UserRepositoryImpl
import com.catnip.foodfood.utils.GenericViewModelFactory
import com.catnip.foodfood.utils.proceedWhen
import com.google.firebase.auth.FirebaseAuth

class EditProfileActivity : AppCompatActivity() {
    private var profPic : Uri? = null

    private val binding: ActivityEditProfileBinding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }

    private val viewModel: EditProfileViewModel by viewModels {
        GenericViewModelFactory.create(createViewModel())
    }

    private fun createViewModel(): EditProfileViewModel {
        val firebaseAuth = FirebaseAuth.getInstance()
        val dataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val repo = UserRepositoryImpl(dataSource)
        return EditProfileViewModel(repo)
    }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            profPic = uri
            binding.ivProfile.load(uri) {
                crossfade(true)
                placeholder(R.drawable.profile)
                error(R.drawable.profile)
                transformations(CircleCropTransformation())
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setClickListeners()
        observeData()
        setupProfile()
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

    private fun setClickListeners() {
        binding.ivEdit.setOnClickListener {
            if (isFormValid()) {
                val email = binding.etEmail.text.toString().trim()
                val password = binding.etPassword.text.toString().trim()
                val username = binding.etUsername.text.toString().trim()
                viewModel.updateProfile(profPic,username,password.ifEmpty { null },email)
            }
        }
        binding.ivProfile.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun observeData() {
        viewModel.changeProfileResult.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    Toast.makeText(this, "Change Profile data Success !", Toast.LENGTH_SHORT).show()
                    binding.cvProgress.visibility= View.GONE
                },
                doOnError = {
                    Toast.makeText(this, "Change Profile data Failed !", Toast.LENGTH_SHORT).show()
                    binding.cvProgress.visibility= View.GONE
                },
                doOnLoading = {
                    binding.cvProgress.visibility= View.VISIBLE
                }
            )
        }
    }
    private fun isFormValid(): Boolean {
        val password:String = binding.etPassword.text.toString().trim()
        val username = binding.etUsername.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        return checkUsernameValidation(username) && checkEmailValidation(email) &&
                checkPasswordValidation(password)

    }

    private fun checkUsernameValidation(username: String): Boolean {
        return if (username.isEmpty()) {
            binding.tilUsername.isErrorEnabled = true
            binding.tilUsername.error = getString(R.string.text_error_name_cannot_empty)
            false
        } else {
            binding.tilUsername.isErrorEnabled = false
            true
        }
    }

    private fun checkEmailValidation(email: String): Boolean {
        return if (email.isEmpty()) {
            binding.tilEmail.isErrorEnabled = true
            binding.tilEmail.error = getString(R.string.text_error_email_empty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.isErrorEnabled = true
            binding.tilEmail.error = getString(R.string.text_error_email_invalid)
            false
        } else {
            binding.tilEmail.isErrorEnabled = false
            true
        }
    }

    private fun checkPasswordValidation(
        password: String
    ): Boolean {
        return if (password.isNotEmpty() && password.length < 8) {
            binding.tilPassword.isErrorEnabled = true
            binding.tilPassword.error =
                getString(R.string.text_error_password_less_than_8_char)
            false
        } else {
            binding.tilPassword.isErrorEnabled = false
            true
        }
    }
}