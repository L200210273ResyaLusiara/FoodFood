package com.catnip.foodfood.presentation.fragmentdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.catnip.foodfood.databinding.FragmentDetailBinding
import com.catnip.foodfood.local.database.entity.Food


class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var food: Food

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        food = DetailFragmentArgs.fromBundle(arguments as Bundle).food!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showMenuData()
        setClickListener()
    }

    private fun setClickListener() {
        var qty = 1
        with(binding){
            tvDescDesc.setOnClickListener {
                navigateToGoogleMaps()
            }
            btnDec.setOnClickListener{
                qty-=1
                qty = if (qty<1) 1 else qty
                tvQty.text=qty.toString()
            }
            btnInc.setOnClickListener{
                qty+=1
                tvQty.text=qty.toString()
            }

        }

    }

    private fun showMenuData() {
        food?.let { p ->
            binding.apply {
                ivFoodImage.load(p.image){
                    crossfade(true)
                }
                tvFoodName.text = p.name
                tvFoodPrice.text = p.price.toString()
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