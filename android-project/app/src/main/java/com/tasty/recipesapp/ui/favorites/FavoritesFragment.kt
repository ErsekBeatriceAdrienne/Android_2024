package com.tasty.recipesapp.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tasty.recipesapp.adapters.RecipeAdapter
import com.tasty.recipesapp.databinding.FragmentFavoritesBinding
import com.tasty.recipesapp.models.recipe.RecipeViewModel
import com.tasty.recipesapp.models.recipe.RecipeViewModelFactory
import com.tasty.recipesapp.repository.LocalRepository
import com.tasty.recipesapp.RecipeApp
import com.tasty.recipesapp.models.recipe.RecipeModel
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favoritesAdapter: RecipeAdapter
    private lateinit var localRepository: LocalRepository

    private val recipeViewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory(localRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        // Initialize the repository
        val recipeDao = (requireActivity().application as RecipeApp).database.recipeDao()
        val favoriteDao = (requireActivity().application as RecipeApp).database.favoriteDao()
        localRepository = LocalRepository(recipeDao, favoriteDao)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {

            favoritesAdapter = RecipeAdapter(mutableListOf())
            binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(context)
            binding.recyclerViewFavorites.adapter = favoritesAdapter

            viewLifecycleOwner.lifecycleScope.launch {
                recipeViewModel.loadFavoriteRecipesFromDatabase()
            }

            // Observe only the favorite recipes
            recipeViewModel.favoriteRecipes.observe(viewLifecycleOwner) { favoriteRecipes ->
                favoriteRecipes.forEach { it.isFavorite = true }
                favoritesAdapter.updateRecipes(favoriteRecipes.toMutableList())
            }

            // Set the listener for toggling the favorite status
            favoritesAdapter.onFavoriteClickListener = { recipe ->
                recipeViewModel.toggleFavorite(recipe)
                if (recipe.isFavorite) {
                    recipeViewModel.addFavorite(recipe)
                } else {
                    recipeViewModel.removeFavorite(recipe)
                }
                favoritesAdapter.updateRecipes(recipeViewModel.favoriteRecipes.value?.toMutableList() ?: mutableListOf())
            }

            favoritesAdapter.onRecipeClickListener = { recipe ->
                val action = FavoritesFragmentDirections
                    .actionFavoritesFragmentToRecipeDetailsFragment(recipe.recipeID.toString())
                findNavController().navigate(action)
            }
        }
    }

    private suspend fun getFavoriteRecipes(): List<RecipeModel> {
        return localRepository.getFavorites()
    }
}
