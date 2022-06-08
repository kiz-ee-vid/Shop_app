package com.example.lab2.ui.profile

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lab2.databinding.FragmentProfileBinding
import com.example.lab2.ui.favorites.FavoritesViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng

class ProfileFragment : Fragment(), OnMapReadyCallback {

    private val _binding: FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(
            layoutInflater
        )
    }
    private val favoritesViewModel by lazy {
        activity?.let { ViewModelProvider(it) }?.get(FavoritesViewModel::class.java)
    }
    private lateinit var nmap: GoogleMap
    private lateinit var mapView: MapView
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.textGmail.text = favoritesViewModel?.user?.login
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        fusedLocationClient =
            activity?.let { LocationServices.getFusedLocationProviderClient(it) }!!
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        println("hello")
        nmap = googleMap
        nmap.setMinZoomPreference(12F)
        setUpMap()
    }

    private fun setUpMap() {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) &&
            (context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED)
        ) {
            nmap.isMyLocationEnabled = true
            activity?.let { location ->
                fusedLocationClient.lastLocation.addOnSuccessListener(location) {
                    if (it != null) {
                        lastLocation = it
                        var currentLatLong = LatLng(it.latitude, it.longitude)
                        nmap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}