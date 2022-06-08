package com.example.lab2.ui.product_page

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.lab2.MainActivity
import com.example.lab2.databinding.FragmentProductBinding
import com.example.lab2.R
import com.example.lab2.domain.ui_model.Product
import com.example.lab2.ui.favorites.FavoritesViewModel

class ProductFragment : Fragment() {

    private val favoritesViewModel by lazy {
        activity?.let { ViewModelProvider(it) }?.get(FavoritesViewModel::class.java)
    }
    private val binding: FragmentProductBinding by lazy {
        FragmentProductBinding.inflate(
            layoutInflater
        )
    }
    private var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (favoritesViewModel?.filterProducts?.value == null)
            favoritesViewModel?.filterProducts?.value = arrayListOf()
//        val ma: AppCompatActivity = activity as AppCompatActivity
//        ma.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).binding
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.apply {
            product = arguments?.getParcelable("product")
            productTitle.text = product?.title ?: "No information"
            productDescription.text = product?.description ?: "No information"
            val image = product?.image
            Glide.with(binding.productImage)
                .load(image)
                .into(binding.productImage)
            productCost.text = product?.let { String.format("%.2f", it.price).plus(" ${product!!.currency}") }
        }

        binding.ButCor.setOnClickListener {
            if(favoritesViewModel?.filterProducts?.value?.contains(product) != true) {
                product?.let { it1 -> favoritesViewModel?.filterProducts?.value?.add(it1) }
                product?.let { it1 -> favoritesViewModel?.saveData(it1) }
                Toast.makeText(context, "Product added", Toast.LENGTH_SHORT).show()
            }
            else
                Toast.makeText(context, "Error: product already added", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        println(item.itemId)
        when (item.itemId) {
            android.R.id.home -> {
                println("Home")
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_navigation_product_to_navigation_home)
            }
        }
        return true
    }

}