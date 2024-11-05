package com.tasty.recipesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tasty.recipesapp.R
import com.tasty.recipesapp.databinding.ItemRecipeBinding
import com.tasty.recipesapp.models.RecipeModel
import com.tasty.recipesapp.repository.RecipeRepository

class RecipeAdapter(
    private var recipes: MutableList<RecipeModel>,
    private val recipeRepository: RecipeRepository,
    private val onRecipeLongClick: (RecipeModel) -> Unit,
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder
    {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position])
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
                if (isFavorite) {
                    recipeRepository.saveFavorite(recipe.recipeID.toString())
                    binding.buttonFavorite.setImageResource(R.drawable.heart_filled)
                } else {
                    recipeRepository.removeFavorite(recipe.recipeID.toString())
                    binding.buttonFavorite.setImageResource(R.drawable.heart_unfilled)
                }
            }

            // Set long click listener to delete the recipe
            binding.root.setOnLongClickListener {
                onRecipeLongClick(recipe)
                true
            }

            binding.executePendingBindings()
        }
    }
}
