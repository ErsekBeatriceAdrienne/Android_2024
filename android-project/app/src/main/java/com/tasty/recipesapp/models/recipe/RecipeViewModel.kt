package com.tasty.recipesapp.models.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasty.recipesapp.database.entities.FavoriteEntity
import com.tasty.recipesapp.database.entities.RecipeEntity
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
            val favoriteRecipesFromRoom = localRepository.getFavorites()

            // Merge recipes with their favorite status
            val updatedRecipes = recipesFromRoom.map { recipe ->
                recipe.copy(isFavorite = favoriteRecipesFromRoom.any { it.recipeID == recipe.recipeID })
            }
            _recipes.postValue(updatedRecipes)
        }
    }

    // Load favorite recipes from the database
    fun loadFavoriteRecipesFromDatabase() {
        viewModelScope.launch {
            val favorites = localRepository.getFavorites()
            _favoriteRecipes.value = favorites.distinctBy { it.recipeID }
        }
    }

    fun toggleFavorite(recipe: RecipeModel) {
        viewModelScope.launch {
            if (isFavorite(recipe.recipeID.toString())) removeFavorite(recipe)
            else addFavorite(recipe)
            loadRecipes()
            loadFavoriteRecipesFromDatabase()
            recipe.isFavorite = !recipe.isFavorite
            _favoriteRecipes.value = _favoriteRecipes.value?.filter { it.isFavorite }?.toList()

        }
    }

    // Delete a recipe from the database
    fun deleteRecipe(recipeEntity: RecipeEntity) {
        viewModelScope.launch {
            localRepository.deleteRecipe(recipeEntity)
            loadRecipes()
        }
    }

    fun deleteRecipeById(recipeID: Int) {
        viewModelScope.launch {
            localRepository.deleteRecipeById(recipeID)
        }
    }

    fun addFavorite(recipe: RecipeModel) {
        viewModelScope.launch {
            val favoriteEntity = FavoriteEntity(recipeId = recipe.recipeID.toLong())
            localRepository.addFavorite(favoriteEntity)
            loadRecipes()
        }
    }

    // Remove recipe from favorites
    fun removeFavorite(recipe: RecipeModel) {
        viewModelScope.launch {
            localRepository.removeFavorite(recipe.recipeID.toLong())
            loadRecipes()
        }
    }

    suspend fun isFavorite(recipeId: String): Boolean {
        return localRepository.isFavorite(recipeId)
    }
}