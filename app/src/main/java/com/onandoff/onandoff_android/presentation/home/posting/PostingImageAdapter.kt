package com.onandoff.onandoff_android.presentation.home.posting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.databinding.ItemImageBinding

class PostingImageAdapter : RecyclerView.Adapter<PostingImageAdapter.Holder>() {

    private val itemList: MutableList<String> = mutableListOf()
    private lateinit var itemClickListener : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class Holder(var binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            Glide.with(itemView)
                .load(item)
                .into(binding.imagePhoto)
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, categoryId: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    fun setItems(item: List<String>) {
        itemList.clear()
        itemList.addAll(item)
    }
}
