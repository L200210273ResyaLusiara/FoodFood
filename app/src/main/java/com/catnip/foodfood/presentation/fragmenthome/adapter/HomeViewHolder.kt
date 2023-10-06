package com.catnip.foodfood.presentation.fragmenthome.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.catnip.foodfood.core.ViewHolderBinder
import com.catnip.foodfood.databinding.ItemGridMenuBinding
import com.catnip.foodfood.databinding.ItemLinearMenuBinding
import com.catnip.foodfood.local.database.entity.Food
import java.text.NumberFormat
import java.util.Locale

class LinearFoodItemViewHolder(
    private val binding : ItemLinearMenuBinding,
    private val onClickListener : (Food) -> Unit
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Food> {
    override fun bind(item: Food) {
        binding.ivFoodImage.load(item.image){
            crossfade(true)
        }
        binding.tvFoodName.text = item.name
        //binding.tvFoodPrice.text = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(item.price)
        binding.root.setOnClickListener{
            onClickListener.invoke(item)
        }
    }
}

class GridFoodItemViewHolder(
    private val binding : ItemGridMenuBinding,
    private val onClickListener : (Food) -> Unit
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Food> {
    override fun bind(item: Food) {
        binding.ivFoodImage.load(item.image){
            crossfade(true)
        }
        binding.tvFoodName.text = item.name
        //binding.tvFoodPrice.text = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(item.price)
        binding.root.setOnClickListener{
            onClickListener.invoke(item)
        }
    }
}