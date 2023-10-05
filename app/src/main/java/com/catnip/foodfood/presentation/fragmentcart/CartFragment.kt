package com.catnip.foodfood.presentation.fragmentcart

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.catnip.foodfood.databinding.FragmentCartBinding
import com.catnip.foodfood.local.database.entity.Cart
import com.catnip.foodfood.local.database.repository.CartRepository
import com.catnip.foodfood.presentation.checkout.CheckoutActivity
import com.catnip.foodfood.presentation.fragmentcart.adapter.CartAdapter
import com.catnip.foodfood.presentation.fragmentcart.adapter.CartListener
import com.catnip.foodfood.utils.GenericViewModelFactory
import java.text.NumberFormat
import java.util.Locale

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding

    private val viewModel: CartViewModel by viewModels {
        GenericViewModelFactory.create(CartViewModel(CartRepository(requireActivity().application)))
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
        viewModel.cartList.observe(viewLifecycleOwner) { result ->
            adapter.submitData(result)
            var total = 0
            for(data in result){
                total+=data.cart.quantity*data.food.price
            }
            binding.tvTotalPrice.text = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(total)
        }
    }

}