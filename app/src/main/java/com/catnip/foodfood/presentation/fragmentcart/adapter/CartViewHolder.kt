package com.catnip.foodfood.presentation.fragmentcart.adapter

import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.catnip.foodfood.core.ViewHolderBinder
import com.catnip.foodfood.databinding.ItemCartBinding
import com.catnip.foodfood.local.database.relation.CartFoodRelation
import java.text.NumberFormat
import java.util.Locale

class CartViewHolder(
    private val binding: ItemCartBinding,
    private val cartListener: CartListener?
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<CartFoodRelation> {
    override fun bind(item: CartFoodRelation) {
        setCartData(item)
        setCartNotes(item)
        setClickListeners(item)
    }

    private fun setCartData(item: CartFoodRelation) {
        with(binding) {
            binding.ivProductImage.load(item.food.image) {
                crossfade(true)
            }
            tvProductCount.text = item.cart.quantity.toString()
            tvProductName.text = item.food.name
            tvProductPrice.text = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(item.cart.quantity * item.food.price)
        }
    }

    private fun setCartNotes(item: CartFoodRelation) {
        binding.etNotesItem.setText(item.cart.notes)
        binding.etNotesItem.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.etNotesItem.clearFocus()
                val newItem = item.cart.copy().apply {
                    notes = binding.etNotesItem.text.toString().trim()
                }
                cartListener?.onUserDoneEditingNotes(newItem)
                true
            }
            false
        }
    }

    private fun setClickListeners(item: CartFoodRelation) {
        with(binding) {
            btnDec.setOnClickListener { cartListener?.onMinusTotalItemCartClicked(item.cart) }
            btnInc.setOnClickListener { cartListener?.onPlusTotalItemCartClicked(item.cart) }
            ivRemoveCart.setOnClickListener { cartListener?.onRemoveCartClicked(item.cart) }
        }
    }
}