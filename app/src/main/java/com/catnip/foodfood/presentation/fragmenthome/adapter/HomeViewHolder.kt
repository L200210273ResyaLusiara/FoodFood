package com.catnip.foodfood.presentation.fragmenthome.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.catnip.foodfood.core.ViewHolderBinder
import com.catnip.foodfood.databinding.ItemGridMenuBinding
import com.catnip.foodfood.databinding.ItemLinearMenuBinding
import com.catnip.foodfood.model.Food
import java.text.NumberFormat
import java.util.Locale

class LinearFoodItemViewHolder(
    private val binding : ItemLinearMenuBinding,
    private val onClickListener : (Food) -> Unit
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Food> {
    override fun bind(item: Food) {
        binding.ivFoodImage.load(item.imageUrl){
            crossfade(true)
        }
        binding.tvFoodName.text = item.nama
        binding.tvFoodPrice.text = item.hargaFormat
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
        binding.ivFoodImage.load(item.imageUrl){
            crossfade(true)
        }
        binding.tvFoodName.text = item.nama
        binding.tvFoodPrice.text = item.hargaFormat
        binding.root.setOnClickListener{
            onClickListener.invoke(item)
        }
    }
}