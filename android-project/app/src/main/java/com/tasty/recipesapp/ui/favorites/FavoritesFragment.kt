package com.tasty.recipesapp.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tasty.recipesapp.adapters.RecipeAdapter
import com.tasty.recipesapp.databinding.FragmentFavoritesBinding
import com.tasty.recipesapp.models.RecipeModel
import com.tasty.recipesapp.models.RecipeViewModel
import com.tasty.recipesapp.models.RecipeViewModelFactory
import com.tasty.recipesapp.repository.RecipeRepository

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favoritesAdapter: RecipeAdapter
    private lateinit var recipeRepository: RecipeRepository

    // Use the factory to instantiate the ViewModel
    private val recipeViewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory(recipeRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        // Initialize the RecipeRepository here
        recipeRepository = RecipeRepository(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapter setup
        favoritesAdapter = RecipeAdapter(mutableListOf(), recipeViewModel.recipeRepository) { recipe ->
            // Handle long-click here, e.g., show delete confirmation
        }

        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewFavorites.adapter = favoritesAdapter

        // Observe favorite recipes
        recipeViewModel.recipes.observe(viewLifecycleOwner) { updatedRecipes ->
            favoritesAdapter.updateRecipes(updatedRecipes.toMutableList()) // Ensure this is a MutableList
        }
    }
}