package com.tasty.recipesapp.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) val favoriteId: Long = 0L,
    val recipeId: Long
)