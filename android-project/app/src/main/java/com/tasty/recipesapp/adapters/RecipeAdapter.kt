package com.tasty.recipesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.tasty.recipesapp.R
import com.tasty.recipesapp.databinding.ItemRecipeBinding
import com.tasty.recipesapp.models.recipe.RecipeModel

class RecipeAdapter(private var recipes: MutableList<RecipeModel>) : RecyclerView.Adapter<RecipeAdapter.RecipesViewHolder>()
{
    // Define a listener for long clicks
    var onRecipeLongClickListener: ((RecipeModel) -> Unit)? = null
    var onRecipeClickListener: ((RecipeModel) -> Unit)? = null
    var onFavoriteClickListener: ((RecipeModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesViewHolder
    {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int)
    {
        val recipe = recipes[position]
        holder.bind(recipe)

        // Set the listener for the "More Details" button
        holder.itemView.findViewById<Button>(R.id.buttonMoreDetails).setOnClickListener {
            onRecipeClickListener?.invoke(recipe)
        }

        holder.binding.buttonFavorite.setOnClickListener {
            onFavoriteClickListener?.invoke(recipe)
        }
    }

    fun updateRecipes(newRecipes: MutableList<RecipeModel>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = recipes.size

    inner class RecipesViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onRecipeClickListener?.invoke(recipes[adapterPosition])
            }

            binding.root.setOnLongClickListener {
                onRecipeLongClickListener?.invoke(recipes[adapterPosition])
                true
            }

            binding.buttonFavorite.setOnClickListener {
                val recipe = recipes[adapterPosition]
                onFavoriteClickListener?.invoke(recipe)
            }
        }

        fun bind(recipe: RecipeModel) {
            binding.recipe = recipe
            binding.executePendingBindings()

            if (recipe.isFavorite) {
                binding.buttonFavorite.setImageResource(R.drawable.heart_filled)
            } else {
                binding.buttonFavorite.setImageResource(R.drawable.heart_unfilled)
            }
        }
    }
}