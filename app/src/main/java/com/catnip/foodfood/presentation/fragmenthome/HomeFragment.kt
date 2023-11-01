package com.catnip.foodfood.presentation.fragmenthome

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.catnip.foodfood.R
import com.catnip.foodfood.api.datasource.FoodApiDataSource
import com.catnip.foodfood.api.service.ApiService
import com.catnip.foodfood.databinding.FragmentHomeBinding
import com.catnip.foodfood.model.Category
import com.catnip.foodfood.model.Food
import com.catnip.foodfood.presentation.fragmentcart.CartViewModel
import com.catnip.foodfood.presentation.fragmenthome.adapter.AdapterLayoutMode
import com.catnip.foodfood.presentation.fragmenthome.adapter.CategoryListAdapter
import com.catnip.foodfood.presentation.fragmenthome.adapter.HomeAdapter
import com.catnip.foodfood.repository.CartRepository
import com.catnip.foodfood.repository.FoodRepository
import com.catnip.foodfood.repository.FoodRepositoryImpl
import com.catnip.foodfood.utils.GenericViewModelFactory
import com.catnip.foodfood.utils.proceedWhen
import com.chuckerteam.chucker.api.ChuckerInterceptor


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels {
        val chuckerInterceptor = ChuckerInterceptor(requireContext().applicationContext)
        val service = ApiService.invoke(chuckerInterceptor)
        val dataSource = FoodApiDataSource(service)
        val repo: FoodRepository =
            FoodRepositoryImpl(dataSource)
        GenericViewModelFactory.create(HomeViewModel(repo))
    }

    private val foodAdapter: HomeAdapter by lazy {
        HomeAdapter(AdapterLayoutMode.LINEAR) { food: Food ->
            navigateToDetail(food)
        }
    }

    private val categoryAdapter: CategoryListAdapter by lazy {
        CategoryListAdapter {
            viewModel.setSelectedCategory(it.nama.lowercase())
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
        setupSwitch()
    }

    private fun setupViewModel() {
        viewModel.homeData.observe(viewLifecycleOwner) {homeData ->
            if (homeData != null) {
                homeData.categories.proceedWhen(doOnSuccess = {
                    binding.pbCategoryLoading.isVisible=false
                    binding.rvCategory.apply {
                        isVisible = true
                        layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
                        adapter = categoryAdapter
                    }
                    homeData.categories.payload?.let { data -> categoryAdapter.submitData(data) }
                }, doOnLoading = {
                    binding.pbCategoryLoading.isVisible=true
                    binding.rvCategory.isVisible = false
                }, doOnError = {
                    binding.rvCategory.isVisible = false
                    binding.pbCategoryLoading.isVisible=false
                })
                homeData.foods.proceedWhen(doOnSuccess = {
                    binding.pbFoodLoading.isVisible = false
                    binding.rvFood.apply {
                        isVisible = true
                        binding.rvFood.adapter = this@HomeFragment.foodAdapter
                    }
                    homeData.foods.payload?.let { data -> foodAdapter.submitData(data) }
                }, doOnLoading = {
                    binding.rvFood.isVisible = false
                    binding.pbFoodLoading.isVisible = true
                }, doOnError = {
                    binding.rvFood.isVisible = false
                    binding.pbFoodLoading.isVisible = false
                })
            }
        }
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

    private fun setupSwitch() {
        val span = if(foodAdapter.adapterLayoutMode == AdapterLayoutMode.LINEAR) 1 else 2
        binding.rvFood.layoutManager = GridLayoutManager(requireContext(),span)
        val gridPref = requireContext().getSharedPreferences("grid", Context.MODE_PRIVATE)
        val editor = gridPref.edit()
        val isGrid = gridPref.getBoolean("isGrid", false)
        (binding.rvFood.layoutManager as GridLayoutManager).spanCount = if (isGrid) 2 else 1
        foodAdapter.adapterLayoutMode = if(isGrid) AdapterLayoutMode.GRID else AdapterLayoutMode.LINEAR
        binding.switchListGrid.isChecked = isGrid
        foodAdapter.refreshList()
        binding.switchListGrid.setOnCheckedChangeListener { _, isChecked ->
            (binding.rvFood.layoutManager as GridLayoutManager).spanCount = if (isChecked) 2 else 1
            foodAdapter.adapterLayoutMode = if(isChecked) AdapterLayoutMode.GRID else AdapterLayoutMode.LINEAR
            foodAdapter.refreshList()
            editor.putBoolean("isGrid", isChecked)
            editor.apply()
        }
    }

}