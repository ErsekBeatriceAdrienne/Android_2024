package com.tasty.recipesapp.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tasty.recipesapp.models.RecipeModel
import java.io.File

class RecipeRepository(private val context: Context)
{
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

    fun removeRecipe(recipe: RecipeModel)
    {
        val recipes = getRecipes().toMutableList()
        recipes.remove(recipe)
        saveRecipes(recipes)
        updateFavoriteRecipes()
    }

    fun toggleFavorite(recipe: RecipeModel)
    {
        val isCurrentlyFavorite = preferences.getBoolean(recipe.recipeID.toString(), false)
        preferences.edit().putBoolean(recipe.recipeID.toString(), !isCurrentlyFavorite).apply()
        updateFavoriteRecipes()
    }

    private fun updateFavoriteRecipes()
    {
        val favoriteRecipes = getRecipes().filter { isFavorite(it.recipeID.toString()) }
        _favoriteRecipesLiveData.postValue(favoriteRecipes)
    }


    fun saveRecipes(recipes: List<RecipeModel>)
    {
        val jsonString = gson.toJson(recipes)
        context.openFileOutput("recipes.json", Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
    }

    fun saveFavorite(recipeId: String)
    {
        preferences.edit().putBoolean(recipeId, true).apply()
    }

    fun removeFavorite(recipeId: String)
    {
        preferences.edit().remove(recipeId).apply()
    }

    fun isFavorite(recipeId: String): Boolean
    {
        return preferences.getBoolean(recipeId, false)
    }

    fun getFavorites(): List<RecipeModel>
    {
        return getRecipes().filter { isFavorite(it.recipeID.toString()) }
    }

}

