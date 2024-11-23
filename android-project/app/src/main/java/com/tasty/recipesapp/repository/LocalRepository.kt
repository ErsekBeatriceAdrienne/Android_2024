package com.tasty.recipesapp.repository
import com.google.gson.Gson
import com.tasty.recipesapp.database.dao.FavoriteDao
import com.tasty.recipesapp.database.dao.RecipeDao
import com.tasty.recipesapp.database.entities.FavoriteEntity
import com.tasty.recipesapp.database.entities.RecipeEntity
import com.tasty.recipesapp.dtos.RecipeDTO
import com.tasty.recipesapp.dtos.toModel
import com.tasty.recipesapp.models.recipe.RecipeModel
import com.tasty.recipesapp.models.recipe.toEntity
import com.tasty.recipesapp.models.recipe.toModel
import org.json.JSONObject

// ... > App Inspection to see all Local Db tables

class LocalRepository(private val recipeDao: RecipeDao, private val favoriteDao: FavoriteDao)
{
    private val gson = Gson()

    suspend fun insertRecipe(recipe: RecipeModel) {
        val recipeCount = recipeDao.getRecipeCount()

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

    suspend fun getFavorites(): List<RecipeModel> {
        val favoriteEntities = favoriteDao.getAllFavorites() // Get all favorite entities
        val favoriteRecipes = mutableListOf<RecipeModel>()

        // For each favorite, get the corresponding RecipeModel
        for (favorite in favoriteEntities) {
            val recipe = recipeDao.getRecipeById(favorite.recipeId.toString())
            recipe?.let { favoriteRecipes.add(it.toModel()) }
        }
        return favoriteRecipes
    }

    suspend fun deleteRecipe(recipe: RecipeEntity) {
        recipeDao.deleteRecipe(recipe)
    }

    suspend fun deleteRecipeById(recipeID: Int) {
        recipeDao.deleteRecipeById(recipeID)
    }

    suspend fun getRecipeById(id: Long): RecipeModel? {
        val recipeEntity = recipeDao.getRecipeById(id.toString()) ?: return null
        val jsonObject = JSONObject(recipeEntity.json)
        jsonObject.put("id", recipeEntity.internalId)
        return gson.fromJson(jsonObject.toString(), RecipeDTO::class.java).toModel()
    }

    suspend fun addFavorite(favorite: FavoriteEntity) {
        favoriteDao.addFavorite(favorite)
    }

    suspend fun updateRecipe(recipe: RecipeModel) {
        recipeDao.update(recipe.toEntity())
    }

    suspend fun removeFavorite(recipeId: Long) {
        favoriteDao.removeFavoriteByRecipeId(recipeId)
    }

    suspend fun isFavorite(recipeId: String): Boolean {
        return favoriteDao.isFavorite(recipeId.toLong())
    }

    suspend fun saveFavorite(recipeId: String) {
        val favoriteEntity = FavoriteEntity(recipeId.toLong(), recipeId.toLong())
        favoriteDao.addFavorite(favoriteEntity)
    }
}