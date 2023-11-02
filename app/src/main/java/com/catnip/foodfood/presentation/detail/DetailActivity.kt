package com.catnip.foodfood.presentation.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load

import com.catnip.foodfood.databinding.ActivityDetailBinding
import com.catnip.foodfood.model.Food
import com.catnip.foodfood.utils.proceedWhen
import com.catnip.foodfood.utils.toCurrencyFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var food: Food? = null

private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        food = intent.extras?.getParcelable(EXTRA_FOOD)
        if(food==null){
            finish()
        }
        viewModel.setFood(food)
        showMenuData()
        setClickListener()
        setViewModel()
    }

    private fun setViewModel() {
        with(viewModel){
            price.observe(this@DetailActivity){
                binding.btnCart.text="Tambahkan ke Keranjang - ${it.toCurrencyFormat()}"
            }
            quantity.observe(this@DetailActivity){
                binding.tvQty.text=it.toString()
            }
            viewModel.addToCartResult.observe(this@DetailActivity) {
                it.proceedWhen(
                    doOnSuccess = {
                        Toast.makeText(this@DetailActivity, "Add to cart success !", Toast.LENGTH_SHORT).show()
                        finish()
                    }, doOnError = {result->
                        Toast.makeText(this@DetailActivity, result.exception?.message.orEmpty(), Toast.LENGTH_SHORT).show()
                    })
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
            }
        }

    }

    private fun showMenuData() {
        food?.let { p ->
            binding.apply {
                ivFoodImage.load(p.imageUrl){
                    crossfade(true)
                }
                tvFoodName.text = p.nama
                tvFoodPrice.text = p.hargaFormat
                tvDescDesc.text = p.detail
            }
        }
    }

    private fun navigateToGoogleMaps() {
        val mapsUrl = food?.alamatResto
        val mapsIntent = Intent(Intent.ACTION_VIEW)
        mapsIntent.data = Uri.parse(mapsUrl)
        startActivity(mapsIntent)
    }

    companion object{
        const val EXTRA_FOOD = "food"
        fun startActivity(context: Context, food: Food) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_FOOD, food)
            context.startActivity(intent)
        }
    }
}