package com.catnip.foodfood.presentation.checkout

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.catnip.foodfood.databinding.ActivityCheckoutBinding
import com.catnip.foodfood.model.Cart
import com.catnip.foodfood.presentation.fragmentcart.adapter.CartAdapter
import com.catnip.foodfood.presentation.fragmentcart.adapter.CartListener
import com.catnip.foodfood.utils.proceedWhen
import com.catnip.foodfood.utils.toCurrencyFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutBinding

    private val viewModel: CheckoutViewModel by viewModels()

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