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
        var recipe = recipes[position]
        holder.bind(recipe)

        holder.itemView.findViewById<Button>(R.id.buttonMoreDetails).setOnClickListener {
            onRecipeClickListener?.invoke(recipe)
        }

        holder.binding.buttonFavorite.setOnClickListener {
            onFavoriteClickListener?.invoke(recipe)
        }

        with(holder.binding) {
            buttonFavorite.setImageResource(
                if (recipe.isFavorite) R.drawable.heart_filled else R.drawable.heart_unfilled
            )

            buttonFavorite.setOnClickListener {
                onFavoriteClickListener?.invoke(recipe)
                recipe.isFavorite = !recipe.isFavorite
                notifyItemChanged(position)
            }
        }
    }

    fun updateRecipes(newRecipes: MutableList<RecipeModel>) {
        recipes.clear()
        recipes.addAll(newRecipes)
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

            binding.buttonFavorite.setImageResource(
                if (recipe.isFavorite) R.drawable.heart_filled else R.drawable.heart_unfilled
            )
        }
    }
}