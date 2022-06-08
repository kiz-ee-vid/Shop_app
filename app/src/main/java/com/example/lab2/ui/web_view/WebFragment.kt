package com.example.lab2.ui.web_view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.lab2.R
import com.example.lab2.databinding.FragmentWebBinding

class WebFragment : Fragment() {

    private var _binding: FragmentWebBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWebBinding.inflate(inflater, container, false)

        binding.webBrowser.loadUrl("https://zaglushka.ru/zaglushka-plastikovaya-vnutrennyaya-s-tolstoi-shlyapkoi-213-mm-dlya-trub-du15-15duchk")
        binding.webBrowser.settings.javaScriptEnabled = true
        binding.webBrowser.webViewClient = WebViewClient()

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        println(item.itemId)
        when (item.itemId) {
            android.R.id.home -> {
                println("Home")
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_navigation_webView_to_navigation_favorites)
            }
        }
        return true
    }
}