package com.tasty.recipesapp.repository

import android.content.Context
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import android.util.Log
import com.tasty.recipesapp.models.RecipeModel

class RecipeRepository(private val context: Context) {
    private val gson = Gson()

    fun getRecipes(): List<RecipeModel> {
        return try {
            val jsonString = context.assets.open("recipes.json").bufferedReader().use { it.readText() }
            val recipeListType = object : TypeToken<List<RecipeModel>>() {}.type
            gson.fromJson(jsonString, recipeListType)
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error loading recipes.json", e)
            emptyList()
        }
    }
}

