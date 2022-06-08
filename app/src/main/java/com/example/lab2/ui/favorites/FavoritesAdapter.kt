package com.example.lab2.ui.favorites

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lab2.databinding.ProductItemBinding
import com.example.lab2.R
import com.example.lab2.domain.ui_model.Product

class FavoritesAdapter(val itemClick: () -> Unit) :
    RecyclerView.Adapter<FavoritesAdapter.ApiListHolder>() {

    private var productsList = ArrayList<Product>()

    inner class ApiListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ProductItemBinding.bind(itemView)
        fun bind(item: Product) {
            Glide.with(binding.productImage)
                .load(item.image)
                .into(binding.productImage)
            binding.productTitle.text = item.title
            binding.productDescription.text = item.description
            binding.productCost.text = String.format("%.2f", item.price).plus(" ${item.currency}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApiListHolder {
        return ApiListHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ApiListHolder, position: Int) {
        holder.bind(productsList[position])
        holder.itemView.setOnClickListener(View.OnClickListener {
            itemClick()
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(products: ArrayList<Product>){
        productsList = products
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return productsList.size
    }
}