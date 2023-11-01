package com.catnip.foodfood.presentation.fragmenthome.adapter;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.catnip.foodfood.databinding.ItemCategoryProductBinding
import com.catnip.foodfood.model.Category

class CategoryListAdapter(private val itemClick: (Category) -> Unit) :
    RecyclerView.Adapter<CategoryListAdapter.ItemCategoryViewHolder>() {

    private val dataDiffer =
        AsyncListDiffer(this, object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(
                oldItem: Category,
                newItem: Category
            ): Boolean {
                return oldItem.nama == newItem.nama && oldItem.imageUrl == newItem.imageUrl
            }

            override fun areContentsTheSame(
                oldItem: Category,
                newItem: Category
            ): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        })

    fun submitData(data: List<Category>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCategoryViewHolder {
        val binding =
            ItemCategoryProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemCategoryViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: ItemCategoryViewHolder, position: Int) {
        holder.bindView(dataDiffer.currentList[position])
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    class ItemCategoryViewHolder(
        private val binding: ItemCategoryProductBinding,
        val itemClick: (Category) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: Category) {
            with(item) {
                binding.ivCategoryIcon.load(item.imageUrl){
                    crossfade(true)
                }

                binding.tvCategoryName.text = item.nama
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }

}