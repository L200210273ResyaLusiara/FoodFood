package com.catnip.foodfood.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.catnip.foodfood.R
import com.catnip.foodfood.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var bind:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setupBottomNav()
    }
    private fun setupBottomNav() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        bind.navView.setupWithNavController(navController)
    }
}

