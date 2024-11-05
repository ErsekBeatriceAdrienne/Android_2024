package com.tasty.recipesapp.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tasty.recipesapp.databinding.FragmentRecipeDetailBinding
import com.tasty.recipesapp.models.RecipeViewModel

class RecipeDetailFragment : Fragment()
{
    private lateinit var binding: FragmentRecipeDetailBinding
    private val recipeViewModel: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val recipeId = arguments?.getString("recipeId")
        val recipe = recipeViewModel.getRecipeById(recipeId)

        // Bind recipe data to the view elements, if the recipe is not null
        recipe?.let {
            binding.recipeTitle.text = it.name
            binding.recipeIngredients.text = it.components.joinToString("\n")
            binding.recipeInstructions.text = it.instructions.joinToString("\n")
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
