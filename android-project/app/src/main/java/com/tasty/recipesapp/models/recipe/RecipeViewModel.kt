package com.tasty.recipesapp.models.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasty.recipesapp.database.entities.FavoriteEntity
import com.tasty.recipesapp.database.entities.RecipeEntity
import com.tasty.recipesapp.repository.roomdatabase.LocalDBRepository
import com.tasty.recipesapp.repository.restapi.RecipeAPIRepository
import kotlinx.coroutines.launch

class RecipeViewModel(private val localRepository: LocalDBRepository) : ViewModel() {

    private val recipeRepository = RecipeAPIRepository()
    private val _recipes = MutableLiveData<List<RecipeModel>>()
    val recipes: LiveData<List<RecipeModel>> get() = _recipes

    private val _favoriteRecipes = MutableLiveData<List<RecipeModel>>()
    val favoriteRecipes: LiveData<List<RecipeModel>> get() = _favoriteRecipes

    init {
        getAllRecipesFromApi()
        loadFavoriteRecipesFromDatabase()
    }

    fun fetchRecipes() {
        viewModelScope.launch {
            try {
                val recipeList = recipeRepository.getRecipesFromApi()
                _recipes.postValue(recipeList)
            } catch (e: Exception) {
                _recipes.postValue(emptyList())  // Handle error by posting an empty list
            }
        }
    }

    // Fetch all recipes from the API
    private fun getAllRecipesFromApi() {
        viewModelScope.launch {
            try {
                val recipesFromApi = recipeRepository.getRecipesFromApi()
                if (recipesFromApi.isNotEmpty()) {
                    _recipes.postValue(recipesFromApi)
                } else {
                    Log.e("RECIPE_API", "No recipes available.")
                    // Show toast for no recipes available
                }
            } catch (e: Exception) {
                Log.e("RECIPE_API", "Error fetching recipes from API", e)
                // Show toast for API error
            }
        }
    }


    // Load recipes from local database if the API call fails or returns no data
    private fun loadRecipesFromDatabase() {
        viewModelScope.launch {
            try {
                val recipesFromRoom = localRepository.getAllRecipes()
                // Update LiveData with recipes from local database
                _recipes.postValue(recipesFromRoom)
            } catch (e: Exception) {
                Log.e("RECIPE_DB", "Error fetching recipes from database", e)
            }
        }
    }

    // Load favorite recipes from the database
    fun loadFavoriteRecipesFromDatabase() {
        viewModelScope.launch {
            try {
                val favorites = localRepository.getFavorites()
                _favoriteRecipes.postValue(favorites.distinctBy { it.recipeID })
            } catch (e: Exception) {
                Log.e("RECIPE_FAV", "Error fetching favorite recipes", e)
            }
        }
    }

    // Toggle favorite status for a recipe
    fun toggleFavorite(recipe: RecipeModel) {
        viewModelScope.launch {
            if (isFavorite(recipe.recipeID.toString())) {
                removeFavorite(recipe)
            } else {
                addFavorite(recipe)
            }
            loadRecipesFromDatabase()
            loadFavoriteRecipesFromDatabase()
            recipe.isFavorite = !recipe.isFavorite
        }
    }

    // Add recipe to favorites
    fun addFavorite(recipe: RecipeModel) {
        viewModelScope.launch {
            val favoriteEntity = FavoriteEntity(recipeId = recipe.recipeID.toLong())
            localRepository.addFavorite(favoriteEntity)
        }
    }

    // Remove recipe from favorites
    fun removeFavorite(recipe: RecipeModel) {
        viewModelScope.launch {
            localRepository.removeFavorite(recipe.recipeID.toLong())
        }
    }

    // Delete a recipe from the local database
    fun deleteRecipe(recipeEntity: RecipeEntity) {
        viewModelScope.launch {
            localRepository.deleteRecipe(recipeEntity)
        }
    }

    // Delete a recipe from the local database by its ID
    fun deleteRecipeById(recipeID: Int) {
        viewModelScope.launch {
            localRepository.deleteRecipeById(recipeID)
        }
    }

    // Check if a recipe is marked as a favorite
    suspend fun isFavorite(recipeId: String): Boolean {
        return localRepository.isFavorite(recipeId)
    }
}