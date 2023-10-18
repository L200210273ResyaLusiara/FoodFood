package com.catnip.foodfood.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.catnip.foodfood.R
import com.catnip.foodfood.data.FirebaseAuthDataSourceImpl
import com.catnip.foodfood.databinding.ActivityLoginBinding
import com.catnip.foodfood.presentation.main.MainActivity
import com.catnip.foodfood.presentation.register.RegisterActivity
import com.catnip.foodfood.repository.UserRepositoryImpl
import com.catnip.foodfood.utils.GenericViewModelFactory
import com.catnip.foodfood.utils.highLightWord
import com.catnip.foodfood.utils.proceedWhen
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val viewModel: LoginViewModel by viewModels {
        GenericViewModelFactory.create(createViewModel())
    }

    private fun createViewModel(): LoginViewModel {
        val firebaseAuth = FirebaseAuth.getInstance()
        val dataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val repo = UserRepositoryImpl(dataSource)
        return LoginViewModel(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setClickListeners()
        observeResult()
        if (viewModel.isUserLoggedIn()) {
            navigateToMain()
        }
    }

    private fun observeResult() {
        viewModel.loginResult.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.pbLoading.isVisible = false
                    binding.btnLogin.isVisible = true
                    navigateToMain()
                },
                doOnError = {
                    binding.pbLoading.isVisible = false
                    binding.btnLogin.isVisible = true
                    Toast.makeText(
                        this,
                        "Login Failed : ${it.exception?.message.orEmpty()}",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                doOnLoading = {
                    binding.pbLoading.isVisible = true
                    binding.btnLogin.isVisible = false
                }
            )
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    private fun setClickListeners() {
        binding.btnLogin.setOnClickListener {
            doLogin()
        }
        binding.tvNavToRegister.highLightWord(getString(R.string.register)) {
            navigateToRegister()
        }
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }

    private fun doLogin() {
        if (isFormValid()) {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            viewModel.doLogin(email, password)
        }
    }

    private fun isFormValid(): Boolean {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        return checkEmailValidation(email) &&
                checkPasswordValidation(password)
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
        confirmPassword: String
    ): Boolean {
        return if (confirmPassword.isEmpty()) {
            binding.tilPassword.isErrorEnabled = true
            binding.tilPassword.error =
                getString(R.string.text_error_password_empty)
            false
        } else {
            binding.tilPassword.isErrorEnabled = false
            true
        }
    }
}