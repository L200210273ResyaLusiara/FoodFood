package com.catnip.foodfood.presentation.fragmentdetail

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.net.http.UrlRequest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.catnip.foodfood.databinding.FragmentDetailBinding
import com.catnip.foodfood.model.Food


class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding

    private val food: Food? by lazy {
        DetailFragmentArgs.fromBundle(arguments as Bundle).food
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showMenuData()
        setClickListener()
    }

    private fun setClickListener() {
        binding.tvDescDesc.setOnClickListener {
            navigateToGoogleMaps()
        }
    }

    private fun showMenuData() {
        food?.let { p ->
            binding.apply {
                ivFoodImage.load(p.image){
                    crossfade(true)
                }
                tvFoodName.text = p.name
                tvFoodPrice.text = p.price
                tvDescDesc.text = p.desc
            }
        }
    }

    private fun navigateToGoogleMaps() {
        val mapsUrl = food?.mapsUrl // Use safe call operator (?.)
        if (mapsUrl != null) {
            val mapsIntent = Intent(Intent.ACTION_VIEW)
            mapsIntent.data = Uri.parse(mapsUrl)
            startActivity(mapsIntent)
        }
    }


}