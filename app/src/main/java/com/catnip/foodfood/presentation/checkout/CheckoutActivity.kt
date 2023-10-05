package com.catnip.foodfood.presentation.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import com.catnip.foodfood.R
import com.catnip.foodfood.databinding.ActivityCheckoutBinding
import com.catnip.foodfood.databinding.ActivityMainBinding
import com.catnip.foodfood.local.database.entity.Cart
import com.catnip.foodfood.local.database.repository.CartRepository
import com.catnip.foodfood.presentation.fragmentcart.CartViewModel
import com.catnip.foodfood.presentation.fragmentcart.adapter.CartAdapter
import com.catnip.foodfood.presentation.fragmentcart.adapter.CartListener
import com.catnip.foodfood.utils.GenericViewModelFactory
import java.text.NumberFormat
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {
    private lateinit var bind: ActivityCheckoutBinding
    private val viewModel: CartViewModel by viewModels {
        GenericViewModelFactory.create(CartViewModel(CartRepository(application)))
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
        bind = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setupList()
        observeData()
        setClickListener()
    }
    private fun setClickListener() {
        bind.btnCheckout
            .setOnClickListener {
                Toast.makeText(this,"Checkout Berhasil",Toast.LENGTH_LONG).show()
                finish()
            }
    }
    private fun setupList() {
        bind.rvCart.itemAnimator = null
        bind.rvCart.adapter = adapter
    }
    private fun observeData() {
        viewModel.cartList.observe(this) { result ->
            adapter.submitData(result)
            var total = 0
            for(data in result){
                total+=data.cart.quantity*data.food.price
            }
            bind.tvTotalPrice.text = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(total)
        }
    }
}