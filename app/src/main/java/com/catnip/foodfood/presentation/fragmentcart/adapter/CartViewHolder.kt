package com.catnip.foodfood.presentation.fragmentcart.adapter

import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.catnip.foodfood.core.ViewHolderBinder
import com.catnip.foodfood.databinding.ItemCartBinding
import com.catnip.foodfood.model.Cart
import com.catnip.foodfood.utils.toCurrencyFormat

class CartViewHolder(
    private val binding: ItemCartBinding,
    private val cartListener: CartListener?
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Cart> {
    override fun bind(item: Cart) {
        setCartData(item)
        setCartNotes(item)
        setClickListeners(item)
    }

    private fun setCartData(item: Cart) {
        with(binding) {
            binding.ivProductImage.load(item.foodImage) {
                crossfade(true)
            }
            tvProductCount.text = item.quantity.toString()
            tvProductName.text = item.foodName
            tvProductPrice.text = (item.quantity * item.foodPrice).toCurrencyFormat()
        }
    }

    private fun setCartNotes(item: Cart) {
        binding.etNotesItem.setText(item.notes)
        binding.etNotesItem.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.etNotesItem.clearFocus()
                val newItem = item.copy().apply {
                    notes = binding.etNotesItem.text.toString().trim()
                }
                cartListener?.onUserDoneEditingNotes(newItem)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun setClickListeners(item: Cart) {
        with(binding) {
            btnDec.setOnClickListener { cartListener?.onMinusTotalItemCartClicked(item) }
            btnInc.setOnClickListener { cartListener?.onPlusTotalItemCartClicked(item) }
            ivRemoveCart.setOnClickListener { cartListener?.onRemoveCartClicked(item) }
        }
    }
}