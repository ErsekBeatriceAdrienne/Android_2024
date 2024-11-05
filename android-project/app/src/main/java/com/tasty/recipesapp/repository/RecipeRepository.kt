package com.tasty.recipesapp.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tasty.recipesapp.models.RecipeModel
import java.io.File

class RecipeRepository(private val context: Context) {

    private val gson = Gson()
    private val recipeFileName = "recipes.json"
    private val preferences = context.getSharedPreferences("favorite_recipes", Context.MODE_PRIVATE)
    private val _favoriteRecipesLiveData = MutableLiveData<List<RecipeModel>>()
    val favoriteRecipesLiveData: LiveData<List<RecipeModel>> get() = _favoriteRecipesLiveData

    init {
        val file = File(context.filesDir, recipeFileName)
        if (!file.exists()) {
            saveRecipes(emptyList())
        }
        updateFavoriteRecipes()
    }

    // Method to fetch all recipes from recipes.json
    fun getRecipes(): List<RecipeModel> {
        return try {
            val file = File(context.filesDir, recipeFileName)
            if (file.exists()) {
                val jsonString = file.readText()
                val recipeListType = object : TypeToken<List<RecipeModel>>() {}.type
                gson.fromJson(jsonString, recipeListType)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error loading $recipeFileName", e)
            emptyList()
        }
    }

    // Method to remove a specific recipe
    fun removeRecipe(recipe: RecipeModel) {
        val recipes = getRecipes().toMutableList()
        recipes.remove(recipe)
        saveRecipes(recipes)
        updateFavoriteRecipes()
    }

    // Method to toggle the favorite status of a recipe
    fun toggleFavorite(recipe: RecipeModel) {
        val isCurrentlyFavorite = preferences.getBoolean(recipe.recipeID.toString(), false)
        preferences.edit().putBoolean(recipe.recipeID.toString(), !isCurrentlyFavorite).apply()
        updateFavoriteRecipes()
    }

    // Method to update the LiveData of favorite recipes
    private fun updateFavoriteRecipes() {
        val favoriteRecipes = getRecipes().filter { isFavorite(it.recipeID.toString()) }
        _favoriteRecipesLiveData.postValue(favoriteRecipes)
    }

    // Method to save the list of recipes to recipes.json
    fun saveRecipes(recipes: List<RecipeModel>) {
        val jsonString = gson.toJson(recipes)
        try {
            context.openFileOutput(recipeFileName, Context.MODE_PRIVATE).use {
                it.write(jsonString.toByteArray())
            }
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error saving $recipeFileName", e)
        }
    }

    // Methods for managing favorites in SharedPreferences
    fun saveFavorite(recipeId: String) {
        preferences.edit().putBoolean(recipeId, true).apply()
    }

    fun removeFavorite(recipeId: String) {
        preferences.edit().remove(recipeId).apply()
    }

    fun isFavorite(recipeId: String): Boolean {
        return preferences.getBoolean(recipeId, false)
    }

    fun getFavorites(): List<RecipeModel> {
        return getRecipes().filter { isFavorite(it.recipeID.toString()) }
    }
}
