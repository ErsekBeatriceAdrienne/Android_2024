package com.tasty.recipesapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.tasty.recipesapp.R
import com.tasty.recipesapp.databinding.ItemRecipeBinding
import com.tasty.recipesapp.models.recipe.RecipeModel
import com.tasty.recipesapp.repository.LocalRepository
import com.tasty.recipesapp.repository.RecipeRepository
import com.tasty.recipesapp.ui.recipe.RecipeFragmentDirections
import com.tasty.recipesapp.ui.favorites.FavoritesFragmentDirections
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RecipeAdapter(
    private var recipes: MutableList<RecipeModel>
) : RecyclerView.Adapter<RecipeAdapter.RecipesViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesViewHolder
    {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int)
    {
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int = recipes.size

    inner class RecipesViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: RecipeModel) {
            binding.recipe = recipe
            binding.executePendingBindings()
        }
    }

    // Update the recipe list
    fun updateRecipes(newRecipes: List<RecipeModel>) {
        recipes = newRecipes.toMutableList()
        notifyDataSetChanged()
    }
}