package com.example.lab2.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lab2.data.ApiItem
import com.example.lab2.databinding.ProductItemBinding
import com.example.lab2.R
import com.example.lab2.domain.ui_model.Product
import java.io.Serializable

class HomeAdapter(val itemClick: (Bundle) -> Unit) :
    RecyclerView.Adapter<HomeAdapter.ApiListHolder>() {

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
        holder.itemView.setOnClickListener(View.OnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("product", productsList[position])
            itemClick(bundle)
        })
        holder.bind(productsList[position])
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