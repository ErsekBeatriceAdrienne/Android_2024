package com.tasty.recipesapp.repository

import com.tasty.recipesapp.restapi.client.RecipeApiClient
import com.tasty.recipesapp.restapi.response.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepository {

    private val recipeApiClient = RecipeApiClient()

    // Fetch recipes from the API
    suspend fun fetchRecipes(): List<Recipe>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = recipeApiClient.getRecipes()
                response?.recipes
            } catch (e: Exception) {
                e.printStackTrace()
                null // Handle error (e.g., return null or empty list)
            }
        }
    }
}
