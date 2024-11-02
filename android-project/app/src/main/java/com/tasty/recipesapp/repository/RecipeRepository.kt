package com.tasty.recipesapp.repository

import android.content.Context
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import android.util.Log
import com.tasty.recipesapp.models.RecipeModel

class RecipeRepository(private val context: Context)
{
    private val gson = Gson()

    fun getRecipes(): List<RecipeModel>
    {
        return try {
            val jsonString = context.assets.open("recipes.json").bufferedReader().use { it.readText() }
            val recipeListType = object : TypeToken<List<RecipeModel>>() {}.type
            gson.fromJson(jsonString, recipeListType)
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error loading recipes.json", e)
            emptyList()
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

