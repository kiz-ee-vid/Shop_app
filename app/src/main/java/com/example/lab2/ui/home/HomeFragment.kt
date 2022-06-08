package com.example.lab2.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab2.MainActivity
import com.example.lab2.R
import com.example.lab2.databinding.FragmentHomeBinding
import com.example.lab2.domain.ui_model.Product
import com.example.lab2.ui.favorites.FavoritesViewModel
import com.google.firebase.database.FirebaseDatabase

import java.util.ArrayList

class HomeFragment : Fragment() {

    private val _binding: FragmentHomeBinding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val productsRecycler: RecyclerView by lazy { _binding.productList }
    private val categoryRecycler: RecyclerView by lazy { _binding.sortProducts }
    lateinit var homeAdapter: HomeAdapter
    lateinit var categoryAdapter: CategoryAdapter
    private var categoryList = ArrayList(listOf("all")).toMutableList()
    private val homeViewModel by lazy {
        activity?.let { ViewModelProvider(it) }?.get(HomeViewModel::class.java)
    }
    private val favoritesViewModel by lazy {
        activity?.let { ViewModelProvider(it) }?.get(FavoritesViewModel::class.java)
    }
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as MainActivity).binding.navView
        super.onCreate(savedInstanceState)
        if (homeViewModel?.api?.value.isNullOrEmpty())
            if (checkInternetConnection())
            homeViewModel?.getData()
            else
                Toast.makeText(context, "No Internet connection", Toast.LENGTH_SHORT).show()
        checkPermission()
        setHasOptionsMenu(true)
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.apply {
            val hash = arguments?.getString("user")
            if (hash != null) {
                favoritesViewModel?.getUser(hash)
            }
        }

        homeAdapter = HomeAdapter(){
            Navigation
                .findNavController(binding.root)
                .navigate(
                    R.id.action_navigation_home_to_navigation_product,
                    it,
                    null
                )
        }
        productsRecycler.adapter = homeAdapter
        productsRecycler.layoutManager = LinearLayoutManager(context)

        val root: View = binding.root
        homeViewModel?.filterProducts?.observe(viewLifecycleOwner) {
            it?.let { it ->

                homeAdapter.addList(it)

                if(categoryList.size == 1) {
                    homeViewModel!!.api.value?.forEach { apiItem ->
                        if (!categoryList.contains(apiItem.category))
                            apiItem.category?.let { it1 -> categoryList.add(it1) }
                    }
                    categoryAdapter = CategoryAdapter(categoryList) { category ->
                        if (category != "all") {
                            val filterList = ArrayList<Product>()
                            homeViewModel!!.api.value?.forEach { product ->
                                if (product.category == category)
                                    filterList.add(product)
                            }
                            println(filterList)
                            homeViewModel!!.filterProducts.value = filterList
                        } else homeViewModel!!.filterProducts.value = homeViewModel!!.api.value
                    }
                    categoryRecycler.adapter = categoryAdapter
                    categoryRecycler.layoutManager =
                        LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                }
                println(categoryList)
            }
        }

        return root
    }

    private fun checkPermission() {
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED)  {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    HomeFragment.LOCATION_REQUEST_CODE
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.dollar -> {
                homeViewModel?.changeCurrency("USD")
                favoritesViewModel?.changeCurrency("USD", homeViewModel?.financeApi)
                homeAdapter.notifyDataSetChanged()
            }
            R.id.euro -> {
                homeViewModel?.changeCurrency("EUR")
                favoritesViewModel?.changeCurrency("EUR", homeViewModel?.financeApi)
                homeAdapter.notifyDataSetChanged()
            }
            R.id.rub -> {
                homeViewModel?.changeCurrency("RUB")
                favoritesViewModel?.changeCurrency("RUB", homeViewModel?.financeApi)
                homeAdapter.notifyDataSetChanged()
            }
            R.id.byn -> {
                homeViewModel?.changeCurrency("BYN")
                favoritesViewModel?.changeCurrency("BYN", homeViewModel?.financeApi)
                homeAdapter.notifyDataSetChanged()
            }
            R.id.fire ->
                light = if (light) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    false
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    true
                }
            R.id.auth -> {
                Navigation
                    .findNavController(binding.root)
                    .navigate(
                        R.id.action_navigation_home_to_navigation_registration,
                        null
                    )
            }
        }
        return true
    }

    private fun checkInternetConnection(): Boolean {
        val connection =
            activity?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connection.activeNetwork != null
    }

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
        private var light = true
    }
}