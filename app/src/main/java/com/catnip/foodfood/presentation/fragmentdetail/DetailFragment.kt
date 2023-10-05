package com.catnip.foodfood.presentation.fragmentdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import coil.load
import com.catnip.foodfood.databinding.FragmentDetailBinding
import com.catnip.foodfood.local.database.AppDatabase
import com.catnip.foodfood.local.database.entity.Food
import com.catnip.foodfood.presentation.fragmenthome.HomeFragment
import com.catnip.foodfood.utils.GenericViewModelFactory
import java.text.NumberFormat
import java.util.Locale


class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var food: Food

    companion object {
        private const val KEY_ID = "food"
        fun newInstance(identifier: String) = HomeFragment().apply {
            arguments = bundleOf(identifier to KEY_ID)
        }
    }
    private val viewModel: DetailViewModel by viewModels {
        GenericViewModelFactory.create(
            DetailViewModel(food, AppDatabase.getInstance(requireContext()).cartDao())
        )
    }

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
        setViewModel()
    }

    private fun setViewModel() {
        with(viewModel){
            price.observe(viewLifecycleOwner){
                binding.btnCart.text="Tambahkan ke Keranjang - ${NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(it)}"
            }
            quantity.observe(viewLifecycleOwner){
                binding.tvQty.text=it.toString()
            }

        }

    }

    private fun setClickListener() {
        with(binding){
            tvDescDesc.setOnClickListener {
                navigateToGoogleMaps()
            }
            btnDec.setOnClickListener{
                viewModel.decrement()
            }
            btnInc.setOnClickListener{
                viewModel.increment()
            }
            btnCart.setOnClickListener{
                viewModel.addToCart()
                Toast.makeText(requireContext(),"Berhasil menambahkan ke keranjang",Toast.LENGTH_LONG).show()
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