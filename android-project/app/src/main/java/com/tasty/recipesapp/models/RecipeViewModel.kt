package com.tasty.recipesapp.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tasty.recipesapp.repository.RecipeRepository

class RecipeViewModel(val recipeRepository: RecipeRepository) : ViewModel() {
    private val _recipes = MutableLiveData<List<RecipeModel>>()
    val recipes: LiveData<List<RecipeModel>> get() = _recipes

    private val _favoriteRecipes = MutableLiveData<List<RecipeModel>>()
    val favoriteRecipes: LiveData<List<RecipeModel>> get() = _favoriteRecipes

    init {
        loadRecipes()
        loadFavoriteRecipes()
    }

    private fun loadRecipes() {
        _recipes.value = recipeRepository.getRecipes()
    }

    private fun loadFavoriteRecipes() {
        _favoriteRecipes.value = recipeRepository.getFavorites()
    }
}