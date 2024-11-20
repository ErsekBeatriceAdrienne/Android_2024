package com.tasty.recipesapp.repository

import com.tasty.recipesapp.database.dao.RecipeDao
import com.tasty.recipesapp.database.entities.FavoriteEntity
import com.tasty.recipesapp.models.recipe.RecipeModel
import com.tasty.recipesapp.models.recipe.toEntity
import com.tasty.recipesapp.models.recipe.toModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepository(private val recipeDao: RecipeDao) {

    // Fetch all recipes from the database
    suspend fun getRecipesFromRoom(): List<RecipeModel> {
        return withContext(Dispatchers.IO) {
            recipeDao.getAllRecipes().map { it.toModel() }
        }
    }

    // Save a recipe to the database
    suspend fun saveRecipe(recipe: RecipeModel) {
        withContext(Dispatchers.IO) {
            recipeDao.insertRecipe(recipe.toEntity())
        }
    }

    // Delete a recipe from the database
    suspend fun deleteRecipe(recipe: RecipeModel) {
        withContext(Dispatchers.IO) {
            recipeDao.deleteRecipe(recipe.toEntity())
        }
    }

    // Check if a recipe exists
    suspend fun getRecipeById(recipeId: String) {
        return withContext(Dispatchers.IO) {
            recipeDao.getRecipeById(recipeId)?.toModel()
        }
    }

}
