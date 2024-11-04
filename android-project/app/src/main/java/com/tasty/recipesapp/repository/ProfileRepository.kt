package com.tasty.recipesapp.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.util.Log
import com.tasty.recipesapp.models.RecipeModel
import java.io.File
import java.io.FileOutputStream

class ProfileRepository(private val context: Context) {
    private val gson = Gson()

    private fun loadRecipesFromJson(filename: String): List<RecipeModel> {
        return try {
            val jsonString = context.assets.open(filename).bufferedReader().use { it.readText() }
            val recipeListType = object : TypeToken<List<RecipeModel>>() {}.type
            gson.fromJson(jsonString, recipeListType)
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error loading $filename", e)
            emptyList()
        }
    }

    fun loadFavoriteRecipes(): List<RecipeModel> = loadRecipesFromJson("my_favorite.json")

    fun addRecipe(recipe: RecipeModel)
    {
        val currentRecipes = loadFavoriteRecipes().toMutableList()
        currentRecipes.add(recipe)
        saveRecipesToJson("my_favorite.json", currentRecipes)
    }

    private fun saveRecipesToJson(filename: String, recipes: List<RecipeModel>) {
        val jsonString = gson.toJson(recipes)
        val file = File(context.filesDir, filename)
        FileOutputStream(file).use { it.write(jsonString.toByteArray()) }
    }
}
