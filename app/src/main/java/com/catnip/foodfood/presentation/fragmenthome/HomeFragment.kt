package com.catnip.foodfood.presentation.fragmenthome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.catnip.foodfood.R
import com.catnip.foodfood.data.FoodDataSource
import com.catnip.foodfood.data.FoodDataSourceImpl
import com.catnip.foodfood.databinding.FragmentHomeBinding
import com.catnip.foodfood.model.Food
import com.catnip.foodfood.presentation.fragmenthome.adapter.AdapterLayoutMode
import com.catnip.foodfood.presentation.fragmenthome.adapter.HomeAdapter


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val datasource: FoodDataSource by lazy {
        FoodDataSourceImpl()
    }
    private val adapter: HomeAdapter by lazy {
        HomeAdapter(AdapterLayoutMode.LINEAR) { food: Food ->
            navigateToDetail(food)
        }
    }

    private fun navigateToDetail(food: Food) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDetailFragment(food)
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
        setupList()
        setupSwitch()
    }

    private fun setupList() {
        val span = if(adapter.adapterLayoutMode == AdapterLayoutMode.LINEAR) 1 else 2
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(),span)
            adapter = this@HomeFragment.adapter
        }
        adapter.submitData(datasource.getFoods())
    }

    private fun setupSwitch() {
        binding.switchListGrid.setOnCheckedChangeListener { _, isChecked ->
            (binding.recyclerView.layoutManager as GridLayoutManager).spanCount = if (isChecked) 2 else 1
            adapter.adapterLayoutMode = if(isChecked) AdapterLayoutMode.GRID else AdapterLayoutMode.LINEAR
            adapter.refreshList()
        }
    }

}