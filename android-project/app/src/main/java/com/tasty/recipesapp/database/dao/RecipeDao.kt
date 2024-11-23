package com.tasty.recipesapp.database.dao
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.tasty.recipesapp.database.entities.RecipeEntity

@Dao
interface RecipeDao
{
    @Insert
    suspend fun insertRecipe(recipe: RecipeEntity)
    @Query("SELECT * FROM recipe WHERE internalId = :id")
    suspend fun getRecipeById(id: String): RecipeEntity?
    @Query("SELECT * FROM recipe")
    suspend fun getAllRecipes(): List<RecipeEntity>
    @Query("SELECT COUNT(*) FROM recipe")
    suspend fun getRecipeCount(): Int
    @Query("DELETE FROM recipe WHERE internalId = :recipeID")
    suspend fun deleteRecipeById(recipeID: Int)
    @Update
    suspend fun update(recipe: RecipeEntity)
    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)
}