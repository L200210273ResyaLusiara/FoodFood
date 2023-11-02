package com.catnip.foodfood.presentation.fragmentcart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.catnip.foodfood.databinding.FragmentCartBinding
import com.catnip.foodfood.model.Cart
import com.catnip.foodfood.presentation.checkout.CheckoutActivity
import com.catnip.foodfood.presentation.fragmentcart.adapter.CartAdapter
import com.catnip.foodfood.presentation.fragmentcart.adapter.CartListener
import com.catnip.foodfood.utils.proceedWhen
import com.catnip.foodfood.utils.toCurrencyFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding

    private val viewModel: CartViewModel by viewModels()

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        observeData()
        setClickListener()
    }

    private fun setClickListener() {
        binding.btnCheckout.setOnClickListener {
            startActivity(Intent(requireContext(),CheckoutActivity::class.java))
        }
    }

    private fun setupList() {
        binding.rvCart.itemAnimator = null
        binding.rvCart.adapter = adapter
    }

    private fun observeData() {
        viewModel.cartList.observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = { result ->
                    binding.cvProgress.isVisible=false
                    binding.rvCart.isVisible=true
                    result.payload?.let { (carts, totalPrice) ->
                        adapter.submitData(carts)
                        binding.tvTotalPrice.text = totalPrice.toCurrencyFormat()
                    }
                    binding.btnCheckout.isEnabled = true
                },
                doOnError = {
                    binding.btnCheckout.isEnabled = false
                },
                doOnLoading = {
                    binding.cvProgress.isVisible=true
                    binding.rvCart.isVisible=false
                    binding.btnCheckout.isEnabled = false
                },
                doOnEmpty = {
                    binding.cvProgress.isVisible=false
                    binding.rvCart.isVisible=false
                    binding.btnCheckout.isEnabled = false
                    binding.tvTotalPrice.text = 0.toCurrencyFormat()
                }
            )
        }
    }

}