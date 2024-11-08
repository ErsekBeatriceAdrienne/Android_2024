package com.tasty.recipesapp.ui.recipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tasty.recipesapp.R
import com.tasty.recipesapp.databinding.FragmentRecipeDetailBinding
import com.tasty.recipesapp.models.recipe.RecipeModel
import com.tasty.recipesapp.repository.RecipeRepository

class RecipeDetailFragment : Fragment()
{
    private lateinit var binding: FragmentRecipeDetailBinding
    private lateinit var recipeRepository: RecipeRepository
    private lateinit var recipe: RecipeModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        recipeRepository = RecipeRepository(requireContext())

        arguments?.getString("recipeId")?.let { recipeId ->
            recipe = recipeRepository.getRecipeById(recipeId) ?: return@let
            displayRecipeDetails()
        }

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    private fun displayRecipeDetails() {
        binding.recipeTitle.text = recipe.name
        binding.recipeIngredients.text = "Ingredients: ${recipe.components}"
        binding.recipeInstructions.text = "Instructions: ${recipe.instructions}"
        binding.recipeNutrition.text = "Nutrition: ${recipe.nutrition}"
    }

}