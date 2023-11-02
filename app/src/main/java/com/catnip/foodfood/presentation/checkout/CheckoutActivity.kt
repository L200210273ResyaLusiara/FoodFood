package com.catnip.foodfood.presentation.checkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.catnip.foodfood.api.datasource.FoodApiDataSource
import com.catnip.foodfood.data.FirebaseAuthDataSourceImpl
import com.catnip.foodfood.databinding.ActivityCheckoutBinding
import com.catnip.foodfood.api.service.ApiService
import com.catnip.foodfood.local.database.AppDatabase
import com.catnip.foodfood.local.database.datasource.CartDataSource
import com.catnip.foodfood.local.database.datasource.CartDatabaseDataSource
import com.catnip.foodfood.model.Cart
import com.catnip.foodfood.repository.CartRepository
import com.catnip.foodfood.presentation.fragmentcart.adapter.CartAdapter
import com.catnip.foodfood.presentation.fragmentcart.adapter.CartListener
import com.catnip.foodfood.repository.CartRepositoryImpl
import com.catnip.foodfood.repository.UserRepositoryImpl
import com.catnip.foodfood.utils.GenericViewModelFactory
import com.catnip.foodfood.utils.proceedWhen
import com.catnip.foodfood.utils.toCurrencyFormat
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.auth.FirebaseAuth

class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutBinding

    private val viewModel: CheckoutViewModel by viewModels {
        val firebaseAuth = FirebaseAuth.getInstance()
        val authDataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val userRepo = UserRepositoryImpl(authDataSource)
        val database = AppDatabase.getInstance(this)
        val cartDao = database.cartDao()
        val cartDataSource: CartDataSource = CartDatabaseDataSource(cartDao)
        val chuckerInterceptor = ChuckerInterceptor(applicationContext)
        val service = ApiService.invoke(chuckerInterceptor)
        val apiDataSource = FoodApiDataSource(service)
        val cartRepo: CartRepository = CartRepositoryImpl(cartDataSource, apiDataSource)
        GenericViewModelFactory.create(CheckoutViewModel(cartRepo,userRepo))
    }

    private val adapter: CartAdapter by lazy {
        CartAdapter(object : CartListener {
            override fun onPlusTotalItemCartClicked(cart: Cart) {
                viewModel.increaseCart(cart)
            }
            override fun onMinusTotalItemCartClicked(cart: Cart) {
                viewModel.decreaseCart(cart)
            }
            override fun onRemoveCartClicked(cart: Cart) {
                viewModel.deleteCart(cart)
            }
            override fun onUserDoneEditingNotes(cart: Cart) {
                viewModel.updateCartNote(cart)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupList()
        observeData()
        setClickListener()
    }
    private fun setClickListener() {
        binding.btnCheckout
            .setOnClickListener {
                viewModel.order()
            }
        binding.ivBack.setOnClickListener{
            finish()
        }
    }
    private fun setupList() {
        binding.rvCart.itemAnimator = null
        binding.rvCart.adapter = adapter
    }
    private fun observeData() {
        viewModel.checkoutResult.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.cvProgress.isVisible=false
                    viewModel.deleteAll()
                },
                doOnError = {
                    binding.cvProgress.isVisible=false
                    Toast.makeText(this, "Checkout Error", Toast.LENGTH_SHORT).show()
                },
                doOnLoading = {
                    binding.cvProgress.isVisible=true
                }
            )
        }
        viewModel.cartList.observe(this) {
            it.proceedWhen(
                doOnSuccess = { result ->
                    binding.cvProgress.isVisible=false
                    binding.rvCart.isVisible=true
                    result.payload?.let { (carts, totalPrice) ->
                        adapter.submitData(carts)
                        binding.tvTotalPrice.text = totalPrice.toCurrencyFormat()
                    }
                },
                doOnError = {
                    finish()
                },
                doOnLoading = {
                    binding.cvProgress.isVisible=true
                    binding.rvCart.isVisible=false
                },
                doOnEmpty = {
                    Toast.makeText(this@CheckoutActivity,"Checkout successfull",Toast.LENGTH_SHORT).show()
                    finish()
                }
            )
        }
    }
}