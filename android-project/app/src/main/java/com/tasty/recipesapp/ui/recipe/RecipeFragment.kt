package com.tasty.recipesapp.ui.recipe

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tasty.recipesapp.R
import com.tasty.recipesapp.adapters.RecipeAdapter
import com.tasty.recipesapp.databinding.FragmentRecipeBinding
import com.tasty.recipesapp.models.recipe.RecipeModel
import com.tasty.recipesapp.models.recipe.RecipeViewModel
import com.tasty.recipesapp.models.recipe.RecipeViewModelFactory
import com.tasty.recipesapp.repository.RecipeRepository
import kotlinx.coroutines.launch

class RecipeFragment : Fragment()
{
    private lateinit var recipeRepository: RecipeRepository
    private lateinit var binding: FragmentRecipeBinding
    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var recipesAdapter: RecipeAdapter
    private lateinit var recipes: MutableList<RecipeModel>
    private lateinit var searchEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = FragmentRecipeBinding.inflate(inflater, container, false)

        initialize()
        searchRecipe()

        recipeViewModel.recipes.observe(viewLifecycleOwner) { updatedRecipes ->
            recipesAdapter.updateRecipes(updatedRecipes.toMutableList())
        }

        return binding.root
    }

    private fun initialize()
    {
        recipeRepository = RecipeRepository(requireContext())
        val viewModelFactory = RecipeViewModelFactory(recipeRepository)
        recipeViewModel = ViewModelProvider(this, viewModelFactory).get(RecipeViewModel::class.java)

        // Initialize the recipes and adapter
        recipes = recipeRepository.getRecipes().toMutableList()
        recipesAdapter = RecipeAdapter(recipes, recipeRepository, { recipe ->
            showDeleteConfirmationDialog(recipe)
        })

        binding.recyclerViewRecipes.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewRecipes.adapter = recipesAdapter

        // Bind the ViewModel
        binding.viewModel = recipeViewModel
        binding.lifecycleOwner = viewLifecycleOwner

    }

    private fun showDeleteConfirmationDialog(recipe: RecipeModel)
    {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Delete Recipe")
            .setMessage("Are you sure you want to delete the recipe '${recipe.name}'?")
            .setIcon(R.drawable.delete)
            .setPositiveButton("Yes") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    recipeViewModel.deleteRecipe(recipe)
                }}
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .setCancelable(true) // Allow dismissing the dialog by tapping outside

        // Create and customize the AlertDialog
        val dialog = builder.create()

        dialog.show()
    }

    private fun searchRecipe()
    {
        // Initialize the search field
        searchEditText = binding.editTextSearch
        // Filter recipes based on search input
        searchEditText.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterRecipes(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private suspend fun deleteRecipe(recipe: RecipeModel)
    {
        recipeRepository.removeRecipe(recipe)
        recipes = recipeRepository.getRecipes().toMutableList()
        recipesAdapter.updateRecipes(recipes)
    }

    private fun filterRecipes(query: String)
    {
        val filteredRecipes = recipes.filter { recipe ->
            recipe.name.contains(query, ignoreCase = true)
        }
        recipesAdapter.updateRecipes(filteredRecipes.toMutableList())
    }
}