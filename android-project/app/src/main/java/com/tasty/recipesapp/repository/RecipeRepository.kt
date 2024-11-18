package com.tasty.recipesapp.repository

import android.content.Context
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tasty.recipesapp.database.RecipeDatabase
import com.tasty.recipesapp.database.dao.RecipeDao
import com.tasty.recipesapp.database.entities.RecipeEntity
import com.tasty.recipesapp.models.recipe.RecipeModel
import com.tasty.recipesapp.models.recipe.toEntity

class RecipeRepository(private val context: Context)
{
    private val gson = Gson()
    private val preferences = context.getSharedPreferences("favorite_recipes", Context.MODE_PRIVATE)
    private val recipeFileName = "recipes.json"
    private val _favoriteRecipesLiveData = MutableLiveData<List<RecipeModel>>()
    private val recipeDao: RecipeDao = RecipeDatabase.getDatabase(context).recipeDao()
    private val localRepository: LocalRepository = LocalRepository(recipeDao)

    // Get recipes from the Room database
    suspend fun getRecipesFromRoom(): LiveData<List<RecipeModel>>
    {
        val recipeEntities = recipeDao.getAllRecipes()
        val recipeModels = convertEntityListToModelList(recipeEntities)

        val liveData = MutableLiveData<List<RecipeModel>>()
        liveData.postValue(recipeModels)
        return liveData
    }

    // Mark a recipe as favorite
     fun saveFavorite(recipeId: String) {
        try {
            preferences.edit().putBoolean(recipeId, true).apply()
            updateFavoriteRecipes()
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error saving recipe as favorite", e)
        }
    }

    // Remove a recipe from favorites
     fun removeFavorite(recipeId: String) {
        try {
            preferences.edit().putBoolean(recipeId, false).apply()
            updateFavoriteRecipes()
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error removing recipe from favorites", e)
        }
    }

    fun getRecipes(): List<RecipeModel>
    {
        return try {
            val jsonString = context.assets.open(recipeFileName).bufferedReader().use { it.readText() }
            val recipeListType = object : TypeToken<List<RecipeModel>>() {}.type
            gson.fromJson(jsonString, recipeListType)
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error loading recipes.json", e)
            emptyList()
        }
    }

    private fun convertEntityListToModelList(entityList: List<RecipeEntity>): List<RecipeModel> {
        val gson = Gson()

        return entityList.map { entity ->
            // Deserialize JSON to RecipeModel
            gson.fromJson(entity.json, RecipeModel::class.java)
        }
    }

    // Save a recipe to the Room database
    suspend fun saveRecipe(recipe: RecipeModel) {
        try {
            recipeDao.insertRecipe(recipe.toEntity())
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error saving recipe to Room", e)
        }
    }

    // Remove recipe from Room database
    suspend fun removeRecipe(recipe: RecipeModel) {
        try {
            // Remove from Room database using LocalRepository
            localRepository.deleteRecipe(recipe)

            // If the recipe was marked as favorite, also remove from favorites
            preferences.edit().putBoolean(recipe.recipeID.toString(), false).apply()

            // Update the list of favorite recipes
            updateFavoriteRecipes()
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error removing recipe", e)
        }
    }

    fun isFavorite(recipeId: String): Boolean {
        return preferences.getBoolean(recipeId, false)
    }

    fun getFavorites(): List<RecipeModel> {
        return getRecipes().filter { isFavorite(it.recipeID.toString()) }
    }

    suspend fun getFavoriteRecipesFromRoom(): List<RecipeModel> {
        val recipeEntities = recipeDao.getAllRecipes()
        val favoriteRecipes = recipeEntities.filter { isFavorite(it.internalId.toString()) }
        return convertEntityListToModelList(favoriteRecipes)
    }

    // Method to update the LiveData of favorite recipes
    private fun updateFavoriteRecipes() {
        val favoriteRecipes = getRecipes().filter { isFavorite(it.recipeID.toString()) }
        _favoriteRecipesLiveData.postValue(favoriteRecipes)
    }

    fun getRecipeById(recipeId: String): RecipeModel? {
        return getRecipes().find { it.recipeID.toString() == recipeId }
    }

}

