package com.tasty.recipesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tasty.recipesapp.database.dao.FavoriteDao
import com.tasty.recipesapp.database.dao.RecipeDao
import com.tasty.recipesapp.database.entities.FavoriteEntity
import com.tasty.recipesapp.database.entities.RecipeEntity

@Database(entities = [RecipeEntity::class, FavoriteEntity::class], version = 1, exportSchema = false)
abstract class RecipeDatabase : RoomDatabase()
{
    abstract fun recipeDao(): RecipeDao
    abstract fun favoriteDao(): FavoriteDao

    companion object
    {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null
        fun getDatabase(context: Context): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "recipe_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }
}
