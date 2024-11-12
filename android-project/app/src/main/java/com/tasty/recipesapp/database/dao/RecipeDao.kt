package com.tasty.recipesapp.database.dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.tasty.recipesapp.database.entities.RecipeEntity

@Dao
interface RecipeDao
{
    @Insert
    suspend fun insertRecipe(recipe: RecipeEntity)
    @Query("SELECT * FROM recipe WHERE internalId = :id")
    suspend fun getRecipeById(id: Long): RecipeEntity?
    @Query("SELECT * FROM recipe")
    suspend fun getAllRecipes(): List<RecipeEntity>
    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)
}