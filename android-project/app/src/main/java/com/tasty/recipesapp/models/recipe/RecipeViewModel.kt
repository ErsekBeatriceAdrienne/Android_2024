package com.tasty.recipesapp.models.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    suspend fun getAllRecipes(): List<RecipeModel> {
        return localRepository.getAllRecipes()
    }

    // Add a recipe to the database
    fun saveRecipe(recipe: RecipeModel) {
        viewModelScope.launch {
            localRepository.insertRecipe(recipe)
            loadRecipes()
        }
    }

    // Delete a recipe from the database
    fun deleteRecipe(recipe: RecipeModel) {
        viewModelScope.launch {
            localRepository.deleteRecipe(recipe)
            loadRecipes()
        }
    }

    // Get a single recipe by its ID
    fun getRecipeById(recipeId: String, callback: (RecipeModel?) -> Unit) {
        viewModelScope.launch {
            val recipe = localRepository.getRecipeById(recipeId.toLong())
            callback(recipe)
        }
    }
}
