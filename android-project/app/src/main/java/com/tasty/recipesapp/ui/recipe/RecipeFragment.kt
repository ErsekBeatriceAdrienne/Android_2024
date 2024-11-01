package com.tasty.recipesapp.ui.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tasty.recipesapp.adapters.RecipeAdapter
import com.tasty.recipesapp.databinding.FragmentRecipeBinding
import com.tasty.recipesapp.models.RecipeViewModel
import com.tasty.recipesapp.models.RecipeViewModelFactory
import com.tasty.recipesapp.repository.RecipeRepository


class RecipeFragment : Fragment() {

    private lateinit var binding: FragmentRecipeBinding
    private lateinit var recipeViewModel: RecipeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipeBinding.inflate(inflater, container, false)

        val recipeRepository = RecipeRepository(requireContext())
        val viewModelFactory = RecipeViewModelFactory(recipeRepository)
        recipeViewModel = ViewModelProvider(this, viewModelFactory).get(RecipeViewModel::class.java)

        // Bind the ViewModel
        binding.viewModel = recipeViewModel
        binding.lifecycleOwner = viewLifecycleOwner


        binding.recyclerViewRecipes.layoutManager = LinearLayoutManager(requireContext())
        recipeViewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            val adapter = RecipeAdapter(recipes)
            binding.recyclerViewRecipes.adapter = adapter
        }

        return binding.root
    }
}
