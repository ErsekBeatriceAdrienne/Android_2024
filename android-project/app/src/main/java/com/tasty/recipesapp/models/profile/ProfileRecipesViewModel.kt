package com.tasty.recipesapp.models.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tasty.recipesapp.models.recipe.RecipeModel
import com.tasty.recipesapp.repository.ProfileRepository

class ProfileRecipesViewModel(private val recipeRepository: ProfileRepository) : ViewModel() {
    private val _recipes = MutableLiveData<List<RecipeModel>>()
    val recipes: LiveData<List<RecipeModel>> get() = _recipes

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        _recipes.value = recipeRepository.loadFavoriteRecipes()
    }
}
