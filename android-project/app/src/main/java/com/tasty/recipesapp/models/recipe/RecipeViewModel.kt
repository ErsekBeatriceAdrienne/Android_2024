package com.tasty.recipesapp.models.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasty.recipesapp.database.entities.FavoriteEntity
import com.tasty.recipesapp.database.entities.RecipeEntity
import com.tasty.recipesapp.repository.LocalRepository
import com.tasty.recipesapp.repository.RecipeRepository
import com.tasty.recipesapp.restapi.response.Recipe
import com.tasty.recipesapp.restapi.response.toModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeViewModel(private val localRepository: LocalRepository) : ViewModel() {

    private val recipeRepository = RecipeRepository()
    private val _recipesAPI = MutableStateFlow<List<Recipe>>(emptyList())
    val recipesAPI: StateFlow<List<Recipe>> get() = _recipesAPI

    private val _recipes = MutableLiveData<List<RecipeModel>>()
    val recipes: LiveData<List<RecipeModel>> get() = _recipes

    private val _favoriteRecipes = MutableLiveData<List<RecipeModel>>()
    val favoriteRecipes: LiveData<List<RecipeModel>> get() = _favoriteRecipes

    init {
        loadRecipes()
        loadFavoriteRecipesFromDatabase()
    }

    private fun fetchRecipesFromApi() {
        viewModelScope.launch {
            val fetchedRecipes = recipeRepository.fetchRecipes()
            fetchedRecipes?.let { recipes ->
                val recipeModels = recipes.map { recipe ->
                    RecipeModel(
                        recipeID = recipe.recipeID, // Map `recipe.recipeID` from API response
                        name = recipe.name, // Map `recipe.name`
                        description = recipe.description, // Map `recipe.description`
                        thumbnailUrl = recipe.thumbnailUrl, // Map `recipe.thumbnailUrl`
                        keywords = recipe.keywords ?: "", // Handle nullable fields
                        isPublic = recipe.isPublic,
                        userEmail = recipe.userEmail ?: "", // Handle nullable fields
                        originalVideoUrl = recipe.originalVideoUrl ?: "",
                        country = recipe.country ?: "",
                        numServings = recipe.numServings,
                        components = recipe.components.map { it.toModel() }, // Convert components
                        instructions = recipe.instructions.map { it.toModel() }, // Convert instructions
                        nutrition = recipe.nutrition.toModel(), // Convert nutrition
                        isFavorite = recipe.isFavorite
                    )
                }
                _recipes.postValue(recipeModels)
                _recipesAPI.value = recipes
            }
        }
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