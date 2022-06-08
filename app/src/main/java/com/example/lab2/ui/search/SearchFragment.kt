package com.example.lab2.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lab2.databinding.FragmentSearchBinding
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab2.R
import com.example.lab2.domain.ui_model.Product
import com.example.lab2.ui.home.HomeAdapter
import com.example.lab2.ui.home.HomeViewModel
import java.util.ArrayList

class SearchFragment : Fragment() {

    private val _binding: FragmentSearchBinding by lazy {
        FragmentSearchBinding.inflate(
            layoutInflater
        )
    }
    private val productsRecycler: RecyclerView by lazy { _binding.productList }
    lateinit var homeAdapter: HomeAdapter
    private val searchViewModel by lazy {
        activity?.let { ViewModelProvider(it) }?.get(HomeViewModel::class.java)
    }
    private val binding get() = _binding!!
    val filterList = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (searchViewModel?.api?.value.isNullOrEmpty())
            if (checkInternetConnection())
                searchViewModel?.getData()
            else
                Toast.makeText(context, "No Internet connection", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root: View = binding.root

        homeAdapter = HomeAdapter() {
            Navigation
                .findNavController(binding.root)
                .navigate(
                    R.id.action_navigation_dashboard_to_navigation_product,
                    it,
                    null
                )
        }
        productsRecycler.adapter = homeAdapter
        productsRecycler.layoutManager = LinearLayoutManager(context)

        searchViewModel?.filterProducts?.observe(viewLifecycleOwner) {
            it?.let { it ->
                if (binding.searchView.query.isNullOrEmpty()) {
                    filterList.clear()
                    filterList.addAll(it)
                }
                homeAdapter.addList(filterList)
                println(it.toString())

            }

            binding.searchView.setOnQueryTextListener(
                object : android.widget.SearchView.OnQueryTextListener {

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        searchViewModel!!.api.value?.forEach { product ->
                            if (product.title == query)
                                filterList.add(product)
                        }
                        if (filterList.isNotEmpty())
                            homeAdapter.notifyDataSetChanged()
                        return false
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onQueryTextChange(newText: String?): Boolean {
                        filterList.clear()
                        searchViewModel!!.api.value?.forEach { product ->
                            if (newText?.let { it1 -> product.title?.contains(it1) } == true)
                                filterList.add(product)
                        }
                        if (filterList.isNotEmpty()) {
                            homeAdapter.notifyDataSetChanged()
                        }
                        return false
                    }
                })
        }
        return root
    }

    private fun checkInternetConnection(): Boolean {
        val connection =
            activity?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connection.activeNetwork != null
    }

}