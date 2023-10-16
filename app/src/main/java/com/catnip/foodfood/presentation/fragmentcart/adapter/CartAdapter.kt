package com.catnip.foodfood.presentation.fragmentcart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.catnip.foodfood.core.ViewHolderBinder
import com.catnip.foodfood.databinding.ItemCartBinding
import com.catnip.foodfood.local.database.entity.Cart

class CartAdapter(private val cartListener: CartListener? = null) :

    RecyclerView.Adapter<ViewHolder>() {
    val cartFoods = arrayListOf<Cart>()
    fun submitData(data: List<Cart>) {
        cartFoods.clear()
        cartFoods.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return CartViewHolder(
            ItemCartBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), cartListener
        )
    }

    override fun getItemCount(): Int = cartFoods.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolderBinder<Cart>).bind(cartFoods[position])
    }

}