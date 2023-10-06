package com.catnip.foodfood.presentation.fragmenthome.adapter;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.catnip.foodfood.databinding.ItemCategoryProductBinding
import com.catnip.foodfood.model.Category

class CategoryListAdapter(private val itemClick: (Category) -> Unit) :
    RecyclerView.Adapter<CategoryListAdapter.ItemCategoryViewHolder>() {

    private var items: MutableList<Category> = mutableListOf()

    fun setItems(items: List<Category>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCategoryViewHolder {
        val binding =
            ItemCategoryProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemCategoryViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: ItemCategoryViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ItemCategoryViewHolder(
        private val binding: ItemCategoryProductBinding,
        val itemClick: (Category) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: Category) {
            with(item) {
                binding.ivCategoryIcon.load(item.categoryImg){
                    crossfade(true)
                }

                binding.tvCategoryName.text = item.name
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }

}