package com.catnip.foodfood.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.catnip.foodfood.R
import com.catnip.foodfood.data.FoodDataSourceImpl
import com.catnip.foodfood.databinding.ActivityMainBinding
import com.catnip.foodfood.local.database.AppDatabase
import com.catnip.foodfood.local.database.entity.Food


class MainActivity : AppCompatActivity() {
    lateinit var bind:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        AppDatabase.getInstance(this).foodDao().insertFoods(FoodDataSourceImpl().getFoods())
        Log.d("TAG", AppDatabase.getInstance(this).foodDao().getAllFoods().toString())
    }
}

