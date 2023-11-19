package com.android.project.tukang.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.project.tukang.databinding.FragmentHomeBinding
import com.android.project.tukang.ui.activity.FillActivity
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        @Suppress("DEPRECATION")
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        bannerCarousel()
        toInsertData()

        return root
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