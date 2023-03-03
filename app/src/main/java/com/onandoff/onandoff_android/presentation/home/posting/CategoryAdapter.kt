package com.onandoff.onandoff_android.presentation.home.posting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.onandoff.onandoff_android.data.model.CategoryData
import com.onandoff.onandoff_android.databinding.ItemCategoryBinding

class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.Holder>() {

    private val itemList: MutableList<CategoryData> = mutableListOf()
    private lateinit var itemClickListener : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class Holder(var binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoryData) {
            binding.categoryName.text = item.categoryName

            itemView.setOnClickListener {
                itemClickListener.onClick(itemView, item.categoryId)
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, categoryId: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    fun setItems(item: List<CategoryData>) {
        itemList.clear()
        itemList.addAll(item)
    }
}