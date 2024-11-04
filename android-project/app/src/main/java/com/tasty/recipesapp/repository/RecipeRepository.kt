package com.tasty.recipesapp.repository

import android.content.Context
import android.os.Build
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import android.util.Log
import androidx.annotation.RequiresApi
import com.tasty.recipesapp.models.RecipeModel
import java.io.File

class RecipeRepository(private val context: Context)
{
    private val gson = Gson()
    private val recipeFileName = "recipes.json"

    init {
        // Ensure the recipes.json file exists
        val file = File(context.filesDir, recipeFileName)
        if (!file.exists()) {
            saveRecipes(emptyList()) // Create an empty file if it does not exist
        }
    }

    fun getRecipes(): List<RecipeModel>
    {
        return try {
            val jsonString = context.openFileInput(recipeFileName).bufferedReader().use { it.readText() }
            val recipeListType = object : TypeToken<List<RecipeModel>>() {}.type
            gson.fromJson(jsonString, recipeListType)
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error loading $recipeFileName", e)
            emptyList()
        }
    }

    fun removeRecipe(recipe: RecipeModel) {
        val recipes = getRecipes().toMutableList()
        recipes.remove(recipe) // Remove the recipe from the list

        // Save the updated recipes back to internal storage
        saveRecipes(recipes)
    }

    fun saveRecipes(recipes: List<RecipeModel>) {
        val jsonString = gson.toJson(recipes)
        context.openFileOutput("recipes.json", Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
    }

    // Favorites
    private val preferences = context.getSharedPreferences("favorite_recipes", Context.MODE_PRIVATE)

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

