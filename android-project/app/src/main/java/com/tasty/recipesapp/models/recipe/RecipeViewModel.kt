package com.tasty.recipesapp.models.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasty.recipesapp.repository.RecipeRepository
import kotlinx.coroutines.launch

class RecipeViewModel(private val recipeRepository: RecipeRepository) : ViewModel()
{
    private val _recipes = MutableLiveData<List<RecipeModel>>()
    val recipes: LiveData<List<RecipeModel>> get() = _recipes

    private val _favoriteRecipes = MutableLiveData<List<RecipeModel>>()
    val favoriteRecipes: LiveData<List<RecipeModel>> get() = _favoriteRecipes

    init
    {
        viewModelScope.launch {
            loadRecipes()
            loadFavoriteRecipes()
            loadRecipesFromRoom()
        }
    }

    // Delete recipe from the database
    suspend fun deleteRecipe(recipe: RecipeModel) {
        recipeRepository.removeRecipe(recipe)
        _recipes.value = _recipes.value?.filter { it.recipeID != recipe.recipeID }
    }

    private fun loadRecipes() {
        _recipes.value = recipeRepository.getRecipes()
    }

    // Load recipes from Room
    private suspend fun loadRecipesFromRoom() {
        recipeRepository.getRecipesFromRoom().observeForever { recipeModels ->
            _recipes.value = _recipes.value?.plus(recipeModels)
        }
    }

    // Load favorite recipes from Room
    fun loadFavoriteRecipesFromDatabase() {
        viewModelScope.launch {
            val favoriteRecipesFromRoom = recipeRepository.getFavoriteRecipesFromRoom()
            _favoriteRecipes.value = _favoriteRecipes.value?.plus(favoriteRecipesFromRoom)
        }
    }

    private fun loadFavoriteRecipes() {
        _favoriteRecipes.value = recipeRepository.getFavorites()
    }
}