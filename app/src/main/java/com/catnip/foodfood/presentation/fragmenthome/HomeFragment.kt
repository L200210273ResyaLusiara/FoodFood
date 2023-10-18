package com.catnip.foodfood.presentation.fragmenthome

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.catnip.foodfood.R
import com.catnip.foodfood.databinding.FragmentHomeBinding
import com.catnip.foodfood.model.Category
import com.catnip.foodfood.model.Food
import com.catnip.foodfood.presentation.fragmenthome.adapter.AdapterLayoutMode
import com.catnip.foodfood.presentation.fragmenthome.adapter.CategoryListAdapter
import com.catnip.foodfood.presentation.fragmenthome.adapter.HomeAdapter


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()

    private val foodAdapter: HomeAdapter by lazy {
        HomeAdapter(AdapterLayoutMode.LINEAR) { food: Food ->
            navigateToDetail(food)
        }
    }

    private val categoryAdapter: CategoryListAdapter by lazy {
        CategoryListAdapter {
            Toast.makeText(binding.root.context, it.nama, Toast.LENGTH_SHORT).show()
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
    }

    private fun setupViewModel() {
        viewModel.getFoods().observe(viewLifecycleOwner) {
            if (it != null) {
                foodAdapter.submitData(it)
            }
        }
        viewModel.getCategories().observe(viewLifecycleOwner){
            if (it != null) {
                categoryAdapter.setItems(it)
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

    private fun setupList() {
        val span = if(foodAdapter.adapterLayoutMode == AdapterLayoutMode.LINEAR) 1 else 2
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(),span)
            binding.recyclerView.adapter = this@HomeFragment.foodAdapter
        }
        binding.rvCategory.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }
        viewModel.setFoods()
        viewModel.setCategories()
    }

    private fun setupSwitch() {
        val gridPref = requireContext().getSharedPreferences("grid", Context.MODE_PRIVATE)
        val editor = gridPref.edit()
        val isGrid = gridPref.getBoolean("isGrid", false)
        (binding.recyclerView.layoutManager as GridLayoutManager).spanCount = if (isGrid) 2 else 1
        foodAdapter.adapterLayoutMode = if(isGrid) AdapterLayoutMode.GRID else AdapterLayoutMode.LINEAR
        binding.switchListGrid.isChecked = isGrid
        foodAdapter.refreshList()
        binding.switchListGrid.setOnCheckedChangeListener { _, isChecked ->
            (binding.recyclerView.layoutManager as GridLayoutManager).spanCount = if (isChecked) 2 else 1
            foodAdapter.adapterLayoutMode = if(isChecked) AdapterLayoutMode.GRID else AdapterLayoutMode.LINEAR
            foodAdapter.refreshList()
            editor.putBoolean("isGrid", isChecked)
            editor.apply()
        }
    }

}