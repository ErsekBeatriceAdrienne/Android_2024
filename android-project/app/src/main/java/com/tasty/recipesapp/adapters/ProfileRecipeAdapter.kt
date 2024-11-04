package com.tasty.recipesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tasty.recipesapp.databinding.ItemRecipeBinding
import com.tasty.recipesapp.models.RecipeModel
import com.tasty.recipesapp.repository.ProfileRepository

class ProfileRecipeAdapter(private var recipes: List<RecipeModel>, private val profileRepository: ProfileRepository) :
    RecyclerView.Adapter<ProfileRecipeAdapter.ProfileRecipesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileRecipesViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileRecipesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileRecipesViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int = recipes.size

    inner class ProfileRecipesViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: RecipeModel) {
            binding.recipe = recipe
            binding.executePendingBindings()
        }
    }

    // Hozzáadva az updateRecipes metódus
    fun updateRecipes(newRecipes: List<RecipeModel>) {
        recipes = newRecipes
        notifyDataSetChanged() // Értesítsd az adaptert a frissítésekről
    }
}
