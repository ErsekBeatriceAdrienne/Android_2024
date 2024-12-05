package com.tasty.recipesapp.ui.recipe

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tasty.recipesapp.R
import com.tasty.recipesapp.adapters.RecipeAdapter
import com.tasty.recipesapp.database.RecipeDatabase
import com.tasty.recipesapp.databinding.FragmentRecipeBinding
import com.tasty.recipesapp.models.recipe.RecipeModel
import com.tasty.recipesapp.models.recipe.RecipeViewModel
import com.tasty.recipesapp.models.recipe.RecipeViewModelFactory
import com.tasty.recipesapp.repository.roomdatabase.LocalDBRepository
import kotlinx.coroutines.launch

class RecipeFragment : Fragment() {
    private lateinit var binding: FragmentRecipeBinding
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var searchEditText: EditText
    private var allRecipes: MutableList<RecipeModel> = mutableListOf()

    private val database by lazy { RecipeDatabase.getDatabase(requireContext()) }
    private val viewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory(LocalDBRepository(database.recipeDao(), database.favoriteDao()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewRecipes.layoutManager = LinearLayoutManager(requireContext())
        recipeAdapter = RecipeAdapter(mutableListOf())
        binding.recyclerViewRecipes.adapter = recipeAdapter

        recipeAdapter.onRecipeLongClickListener = { recipe ->
            showDeleteConfirmationDialog(recipe)
        }

        recipeAdapter.onRecipeClickListener = { recipe ->
            val action = RecipeFragmentDirections.actionRecipeFragmentToRecipeDetailFragment(recipe.recipeID.toString())
            findNavController().navigate(action)
        }

        // Plus button click listener to show the Add Recipe dialog
        binding.imageButtonAddRecipe.setOnClickListener {
            findNavController().navigate(R.id.action_recipeFragment_to_addNewRecipeAPIFragment)
        }

        // Set click listener for the favorite button
        recipeAdapter.onFavoriteClickListener = { recipe ->
            if (recipe.isFavorite) viewModel.removeFavorite(recipe)
            else viewModel.addFavorite(recipe)

            recipe.isFavorite = !recipe.isFavorite
            val position = allRecipes.indexOf(recipe)
            recipeAdapter.notifyItemChanged(position)
        }

        // Observe the ViewModel for updates
        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            if (recipes.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "No recipes available", Toast.LENGTH_SHORT).show()
                binding.recyclerViewRecipes.visibility = View.GONE
            } else {
                binding.recyclerViewRecipes.visibility = View.VISIBLE
                allRecipes.clear() // Clear the existing list
                allRecipes.addAll(recipes) // Populate with new data
                recipeAdapter.updateRecipes(allRecipes.toMutableList()) // Update the adapter
            }
        }

        viewModel.fetchRecipes()
        // Initialize search functionality
        searchRecipe()
    }

    private fun showDeleteConfirmationDialog(recipe: RecipeModel) {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Delete Recipe")
            .setMessage("Are you sure you want to delete the recipe '${recipe.name}'?")
            .setIcon(R.drawable.delete)
            .setPositiveButton("Yes") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        viewModel.deleteRecipeById(recipe.recipeID)
                        allRecipes.remove(recipe)
                        recipeAdapter.updateRecipes(allRecipes)

                        Toast.makeText(requireContext(), "Recipe deleted", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Error deleting recipe", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .setCancelable(true)

        val dialog = builder.create()
        dialog.show()
    }

    private fun searchRecipe() {
        searchEditText = binding.editTextSearch
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterRecipes(s.toString()) // Call filter on text change
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterRecipes(query: String) {
        val filteredRecipes = allRecipes.filter { recipe ->
            recipe.name.contains(query, ignoreCase = true)
        }
        recipeAdapter.updateRecipes(filteredRecipes.toMutableList())
        updateUIBasedOnRecipes(filteredRecipes)
    }

    private fun updateUIBasedOnRecipes(recipes: List<RecipeModel>) {
        if (recipes.isEmpty()) binding.recyclerViewRecipes.visibility = View.GONE
        else binding.recyclerViewRecipes.visibility = View.VISIBLE
    }
}