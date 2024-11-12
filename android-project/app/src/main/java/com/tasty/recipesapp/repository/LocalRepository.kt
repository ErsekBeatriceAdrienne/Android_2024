package com.tasty.recipesapp.repository
import com.google.gson.Gson
import com.tasty.recipesapp.database.dao.RecipeDao
import com.tasty.recipesapp.database.entities.RecipeEntity
import com.tasty.recipesapp.dtos.RecipeDTO
import com.tasty.recipesapp.dtos.toModel
import com.tasty.recipesapp.models.recipe.RecipeModel
import org.json.JSONObject

// ... > App Inspection to see all Local Db tables

class LocalRepository(private val recipeDao: RecipeDao)
{
    private val gson = Gson()

    suspend fun insertRecipe(recipe: RecipeEntity)
    {
        recipeDao.insertRecipe(recipe)
    }

    suspend fun getAllRecipes(): List<RecipeModel>
    {
        return recipeDao.getAllRecipes().map {
            val jsonObject = JSONObject(it.json)
            jsonObject.apply { put("id", it.internalId) }
            gson.fromJson(jsonObject.toString(), RecipeDTO::class.java).toModel()
        }
    }

    suspend fun deleteRecipe(recipe: RecipeEntity)
    {
        recipeDao.deleteRecipe(recipe)
    }

    suspend fun getRecipeById(id: Long)
    {
        recipeDao.getRecipeById(id)
    }
}