package com.example.lab2.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab2.R
import com.example.lab2.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    private val binding: FragmentFavoritesBinding by lazy { FragmentFavoritesBinding.inflate(layoutInflater) }
    private val favoritesViewModel by lazy {
        activity?.let { ViewModelProvider(it) }?.get(FavoritesViewModel::class.java)
    }
    private val productsRecycler: RecyclerView by lazy { binding.favoritesList}
    lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        favoritesAdapter = FavoritesAdapter(){
            Navigation
                .findNavController(binding.root)
                .navigate(
                    R.id.action_navigation_favorites_to_navigation_webView,
                    null
                )
        }
        productsRecycler.adapter = favoritesAdapter
        productsRecycler.layoutManager = LinearLayoutManager(context)

        favoritesViewModel?.filterProducts?.observe(viewLifecycleOwner) {
            it?.let { it ->
                println(it.toString())
                favoritesAdapter.addList(it)
            }
        }

        return binding.root
    }
}