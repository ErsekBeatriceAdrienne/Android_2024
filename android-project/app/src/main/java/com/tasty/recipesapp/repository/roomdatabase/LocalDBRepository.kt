package com.tasty.recipesapp.repository.roomdatabase
import com.google.gson.Gson
import com.tasty.recipesapp.database.dao.FavoriteDao
import com.tasty.recipesapp.database.dao.RecipeDao
import com.tasty.recipesapp.database.entities.FavoriteEntity
import com.tasty.recipesapp.database.entities.RecipeEntity
import com.tasty.recipesapp.dtos.RecipeDTO
import com.tasty.recipesapp.dtos.toModel
import com.tasty.recipesapp.models.recipe.RecipeModel
import com.tasty.recipesapp.models.recipe.toEntity
import org.json.JSONObject

// ... > App Inspection to see all Local Db tables

class LocalDBRepository(private val recipeDao: RecipeDao, private val favoriteDao: FavoriteDao)
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
        return favoriteDao.getAllFavorites().map { favorite ->
            RecipeModel(
                recipeID = favorite.recipeId.toInt(),
                name = favorite.name,
                thumbnailUrl = favorite.thumbnailUrl,
                isFavorite = true
            )
        }
    }

    suspend fun deleteRecipe(recipe: RecipeEntity) {
        recipeDao.deleteRecipe(recipe)
    }

    suspend fun deleteRecipeById(recipeID: Int) {
        recipeDao.deleteRecipeById(recipeID)
    }

    suspend fun addFavorite(favorite: FavoriteEntity) {
        favoriteDao.addFavorite(favorite)
    }

    suspend fun removeFavorite(recipeId: Long) {
        favoriteDao.removeFavoriteByRecipeId(recipeId)
    }

    suspend fun isFavorite(recipeId: String): Boolean {
        return favoriteDao.isFavorite(recipeId.toLong())
    }

}