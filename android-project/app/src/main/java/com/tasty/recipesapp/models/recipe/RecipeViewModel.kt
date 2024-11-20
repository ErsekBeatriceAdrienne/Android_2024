package com.tasty.recipesapp.models.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasty.recipesapp.database.entities.FavoriteEntity
import com.tasty.recipesapp.repository.LocalRepository
import kotlinx.coroutines.launch

class RecipeViewModel(private val localRepository: LocalRepository) : ViewModel() {

    private val _recipes = MutableLiveData<List<RecipeModel>>()
    val recipes: LiveData<List<RecipeModel>> get() = _recipes

    private val _favoriteRecipes = MutableLiveData<List<RecipeModel>>()
    val favoriteRecipes: LiveData<List<RecipeModel>> get() = _favoriteRecipes

    init {
        loadRecipes()
        loadFavoriteRecipesFromDatabase()
    }

    // Load recipes from the database
    private fun loadRecipes() {
        viewModelScope.launch {
            val recipesFromRoom = localRepository.getAllRecipes()
            _recipes.postValue(recipesFromRoom)
        }
    }

    // Load favorite recipes from the database
    fun loadFavoriteRecipesFromDatabase() {
        viewModelScope.launch {
            val favoriteRecipesFromRoom = localRepository.getFavorites()
            _favoriteRecipes.postValue(favoriteRecipesFromRoom)
        }
    }

    // Delete a recipe from the database
    fun deleteRecipe(recipe: RecipeModel) {
        viewModelScope.launch {
            localRepository.deleteRecipe(recipe)
            loadRecipes()
        }
    }

    fun addFavorite(recipe: RecipeModel) {
        viewModelScope.launch {
            val favoriteEntity = FavoriteEntity(recipeId = recipe.recipeID.toLong())
            localRepository.addFavorite(favoriteEntity)
            loadFavoriteRecipesFromDatabase()
        }
    }

    // Remove recipe from favorites
    fun removeFavorite(recipe: RecipeModel) {
        viewModelScope.launch {
            localRepository.removeFavorite(recipe.recipeID.toLong())
            loadFavoriteRecipesFromDatabase()
        }
    }

    suspend fun isFavorite(recipeId: String): Boolean {
        return localRepository.isFavorite(recipeId)
    }
}