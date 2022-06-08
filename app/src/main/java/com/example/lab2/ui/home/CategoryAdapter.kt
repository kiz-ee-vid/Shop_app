package com.example.lab2.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lab2.R
import com.example.lab2.databinding.CategoryItemBinding

class CategoryAdapter(var categoryList: List<String>, val itemClick: (String) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class CategoryListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CategoryItemBinding.bind(itemView)
        fun bind(item: String) {
            binding.categoryTittle.text = item

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CategoryListHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CategoryListHolder).bind(categoryList[position])
        holder.itemView.setOnClickListener {
            itemClick(categoryList[position])
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}