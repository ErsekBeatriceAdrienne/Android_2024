package com.tasty.recipesapp.repository
import com.google.gson.Gson
import com.tasty.recipesapp.database.dao.RecipeDao
import com.tasty.recipesapp.database.entities.RecipeEntity
import com.tasty.recipesapp.dtos.RecipeDTO
import com.tasty.recipesapp.dtos.toModel
import com.tasty.recipesapp.models.recipe.RecipeModel
import com.tasty.recipesapp.models.recipe.toEntity
import org.json.JSONObject

// ... > App Inspection to see all Local Db tables

class LocalRepository(private val recipeDao: RecipeDao)
{
    private val gson = Gson()

    suspend fun insertRecipe(recipe: RecipeModel) {
        val recipeCount = recipeDao.getRecipeCount()

        if (recipeCount == 0) recipe.recipeID = 7
        else recipe

        val recipeEntity = recipe.toEntity()
        recipeDao.insertRecipe(recipeEntity)
    }

    suspend fun getAllRecipes(): List<RecipeModel>
    {
        return recipeDao.getAllRecipes().map {
            val jsonObject = JSONObject(it.json)
            jsonObject.apply { put("id", it.internalId) }
            gson.fromJson(jsonObject.toString(), RecipeDTO::class.java).toModel()
        }
    }

    suspend fun deleteRecipe(recipe: RecipeModel) {
        val recipeEntity = recipe.toEntity()
        recipeDao.deleteRecipe(recipeEntity)
    }

    suspend fun getRecipeById(id: Long): RecipeModel? {
        val recipeEntity = recipeDao.getRecipeById(id) ?: return null
        val jsonObject = JSONObject(recipeEntity.json)
        jsonObject.put("id", recipeEntity.internalId)
        return gson.fromJson(jsonObject.toString(), RecipeDTO::class.java).toModel()
    }
}