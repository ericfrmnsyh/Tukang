package com.android.project.tukang.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.project.tukang.databinding.FragmentHomeBinding
import com.android.project.tukang.ui.activity.FillActivity
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class HomeFragment : Fragment() {

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var latLon: Array<Double>? = arrayOf(0.0,0.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        @Suppress("DEPRECATION")
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        bannerCarousel()
        toInsertData()

        getMyLastLocation()

        return root
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
    @SuppressLint("SetTextI18n")
    private fun getMyLastLocation() {
        if     (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            mFusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val list: List<Address>? =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    binding.tvLocationLabel.text = list?.get(0)?.locality
                    binding.tvLocation.text = list?.get(0)?.getAddressLine(0)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun bannerCarousel() {
        val slideImages = ArrayList<SlideModel>()
        //Sample data
        slideImages.add(SlideModel("https://imgur.com/4ogrrbr.jpeg"))
        slideImages.add(SlideModel("https://imgur.com/D9E0DEY.png"))
        slideImages.add(SlideModel("https://imgur.com/HtddSKm.jpeg"))
        slideImages.add(SlideModel("https://imgur.com/vs5pvZa.jpeg"))

        binding.imageSlider.setImageList(slideImages, ScaleTypes.CENTER_CROP)
    }

    private fun toInsertData(){
        binding.card2.setOnClickListener{
            val intent = Intent(context, FillActivity::class.java)
            startActivity(intent)
        }
        binding.card3.setOnClickListener{
            val intent = Intent(context, FillActivity::class.java)
            startActivity(intent)
        }
        binding.card4.setOnClickListener{
            val intent = Intent(context, FillActivity::class.java)
            startActivity(intent)
        }
        binding.card5.setOnClickListener{
            val intent = Intent(context, FillActivity::class.java)
            startActivity(intent)
        }
        binding.card6.setOnClickListener{
            val intent = Intent(context, FillActivity::class.java)
            startActivity(intent)
        }
        binding.card7.setOnClickListener{
            val intent = Intent(context, FillActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}