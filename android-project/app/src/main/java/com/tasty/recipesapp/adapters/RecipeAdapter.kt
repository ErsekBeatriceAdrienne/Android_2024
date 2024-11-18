package com.tasty.recipesapp.adapters

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
import com.tasty.recipesapp.repository.RecipeRepository
import com.tasty.recipesapp.ui.recipe.RecipeFragmentDirections
import com.tasty.recipesapp.ui.favorites.FavoritesFragmentDirections
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RecipeAdapter(
    private var recipes: MutableList<RecipeModel>,
    private val recipeRepository: RecipeRepository,
    private val onRecipeLongClick: (RecipeModel) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder
    {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int)
    {
        val recipe = recipes[position]
        holder.bind(recipe)
    }

    override fun getItemCount(): Int = recipes.size

    fun updateRecipes(newRecipes: MutableList<RecipeModel>)
    {
        recipes = newRecipes
        notifyDataSetChanged()
    }

    inner class RecipeViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root)
    {
        fun bind(recipe: RecipeModel)
        {
            binding.recipe = recipe

            // Set favorite button state
            var isFavorite = recipeRepository.isFavorite(recipe.recipeID.toString())
            binding.buttonFavorite.setImageResource(
                if (isFavorite) R.drawable.heart_filled else R.drawable.heart_unfilled
            )

            binding.buttonFavorite.setOnClickListener {
                isFavorite = !isFavorite
                val lifecycleOwner = binding.root.findFragment<Fragment>().viewLifecycleOwner
                lifecycleOwner.lifecycleScope.launch {
                    if (isFavorite) {
                        recipeRepository.saveFavorite(recipe.recipeID.toString())
                    } else {
                        recipeRepository.removeFavorite(recipe.recipeID.toString())
                    }
                }
                binding.buttonFavorite.setImageResource(
                    if (isFavorite) R.drawable.heart_filled else R.drawable.heart_unfilled
                )
            }


            // Set long click listener to delete the recipe
            binding.root.setOnLongClickListener {
                onRecipeLongClick(recipe)
                true
            }

            binding.buttonMoreDetails.setOnClickListener {
                val currentFragment = binding.root.findFragment<Fragment>()
                val navController = findNavController(currentFragment)

                val action = when (currentFragment) {
                    is com.tasty.recipesapp.ui.recipe.RecipeFragment -> {
                        RecipeFragmentDirections.actionRecipeFragmentToRecipeDetailFragment(recipe.recipeID.toString())
                    }
                    is com.tasty.recipesapp.ui.favorites.FavoritesFragment -> {
                        FavoritesFragmentDirections.actionFavoritesFragmentToRecipeDetailsFragment(recipe.recipeID.toString())
                    }
                    else -> null
                }


                action?.let { navController.navigate(it) }
            }


            binding.executePendingBindings()
        }
    }
}