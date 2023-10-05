package com.catnip.foodfood.presentation.fragmenthome

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.catnip.foodfood.R
import com.catnip.foodfood.data.CategoryDataSource
import com.catnip.foodfood.data.CategoryDataSourceImpl
import com.catnip.foodfood.data.FoodDataSource
import com.catnip.foodfood.data.FoodDataSourceImpl
import com.catnip.foodfood.databinding.FragmentHomeBinding
import com.catnip.foodfood.local.database.AppDatabase
import com.catnip.foodfood.local.database.entity.Food
import com.catnip.foodfood.presentation.fragmenthome.adapter.AdapterLayoutMode
import com.catnip.foodfood.presentation.fragmenthome.adapter.CategoryListAdapter
import com.catnip.foodfood.presentation.fragmenthome.adapter.HomeAdapter


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    private val datasource: FoodDataSource by lazy {
        FoodDataSourceImpl()
    }
    private val categoryDatasource: CategoryDataSource by lazy {
        CategoryDataSourceImpl()
    }
    private val adapter: HomeAdapter by lazy {
        HomeAdapter(AdapterLayoutMode.LINEAR) { food: Food ->
            navigateToDetail(food)
        }
    }

    private fun navigateToDetail(food: Food) {
        findNavController().navigate(
            HomeFragmentDirections.actionNavigationHomeToDetailFragment(food)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupImage()
        setupViewModel()
        setupList()
        setupSwitch()
        setupCategory()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[HomeViewModel::class.java]
        viewModel.getFoods().observe(requireActivity()) {
            if (it != null) {
                adapter.submitData(it)
            }
        }
    }

    private fun setupCategory() {
        binding.rvCategory.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
        val adapter = CategoryListAdapter {
            Toast.makeText(binding.root.context, it.name, Toast.LENGTH_SHORT).show()
        }
        binding.rvCategory.adapter = adapter
        adapter.setItems(categoryDatasource.getCategories())
    }

    private fun setupImage() {
        Glide.with(requireContext())
            .load(R.drawable.img_diskon)
            .into(binding.diskonImage)
        Glide.with(requireContext()).load(R.drawable.background_hero)
            .into(object : SimpleTarget<Drawable?>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        binding.llBanner.background = resource
                    }
                }
            })
    }

    private fun setupList() {
        val span = if(adapter.adapterLayoutMode == AdapterLayoutMode.LINEAR) 1 else 2
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(),span)
            binding.recyclerView.adapter = this@HomeFragment.adapter
        }
        viewModel.setFoods(requireContext())
    }

    private fun setupSwitch() {
        val gridPref = requireContext().getSharedPreferences("grid", Context.MODE_PRIVATE)
        val editor = gridPref.edit()
        val isGrid = gridPref.getBoolean("isGrid", false)
        (binding.recyclerView.layoutManager as GridLayoutManager).spanCount = if (isGrid) 2 else 1
        adapter.adapterLayoutMode = if(isGrid) AdapterLayoutMode.GRID else AdapterLayoutMode.LINEAR
        binding.switchListGrid.isChecked = isGrid
        adapter.refreshList()
        binding.switchListGrid.setOnCheckedChangeListener { _, isChecked ->
            (binding.recyclerView.layoutManager as GridLayoutManager).spanCount = if (isChecked) 2 else 1
            adapter.adapterLayoutMode = if(isChecked) AdapterLayoutMode.GRID else AdapterLayoutMode.LINEAR
            adapter.refreshList()
            editor.putBoolean("isGrid", isChecked)
            editor.apply()
        }
    }

}