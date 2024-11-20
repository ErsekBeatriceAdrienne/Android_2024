package com.tasty.recipesapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tasty.recipesapp.database.entities.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert
    suspend fun addFavorite(favorite: FavoriteEntity)
    @Query("SELECT * FROM favorites")
    suspend fun getAllFavorites(): List<FavoriteEntity>
    @Query("DELETE FROM favorites WHERE recipeId = :recipeId")
    suspend fun removeFavoriteByRecipeId(recipeId: Long)
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE recipeId = :recipeId)")
    suspend fun isFavorite(recipeId: Long): Boolean
}