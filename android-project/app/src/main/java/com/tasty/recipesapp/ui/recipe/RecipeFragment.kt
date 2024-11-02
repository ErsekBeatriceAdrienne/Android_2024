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

    private lateinit var recipeRepository: RecipeRepository
    private lateinit var binding: FragmentRecipeBinding
    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var recipesAdapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = FragmentRecipeBinding.inflate(inflater, container, false)

        recipeRepository = RecipeRepository(requireContext())
        val viewModelFactory = RecipeViewModelFactory(recipeRepository)
        recipeViewModel = ViewModelProvider(this, viewModelFactory).get(RecipeViewModel::class.java)

        // Favorites
        val recipes = recipeRepository.getRecipes()

        recipesAdapter = RecipeAdapter(recipes, recipeRepository)
        binding.recyclerViewRecipes.adapter = recipesAdapter

        // Bind the ViewModel
        binding.viewModel = recipeViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.recyclerViewRecipes.layoutManager = LinearLayoutManager(requireContext())
        recipeViewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            val adapter = RecipeAdapter(recipes, recipeRepository)
            binding.recyclerViewRecipes.adapter = adapter
        }

        recipeViewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            recipesAdapter.updateRecipes(recipes)
        }

        binding.buttonRecipeOfTheDay.setOnClickListener {
            recipeViewModel.getRandomRecipe()

            binding.cardViewRandomRecipe.visibility = View.VISIBLE
            binding.textViewRandomRecipe.visibility = View.VISIBLE
            binding.textViewRandomRecipeDescription.visibility = View.VISIBLE

            recipeViewModel.randomRecipe.observe(viewLifecycleOwner) { recipe ->
                binding.textViewRandomRecipe.text = recipe?.name ?: "No recipe found!"
                binding.textViewRandomRecipeDescription.text =  " - ${recipe?.description}" ?: ""
            }
        }

        recipeViewModel.randomRecipe.observe(viewLifecycleOwner) { recipe ->
            binding.textViewRandomRecipe.text = recipe?.name ?: "No recipe found!"
        }

        return binding.root
    }
}
