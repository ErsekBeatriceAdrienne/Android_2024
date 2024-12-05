package com.tasty.recipesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tasty.recipesapp.database.dao.FavoriteDao
import com.tasty.recipesapp.database.dao.RecipeDao
import com.tasty.recipesapp.database.entities.FavoriteEntity
import com.tasty.recipesapp.database.entities.RecipeEntity

@Database(entities = [RecipeEntity::class, FavoriteEntity::class], version = 2, exportSchema = false)
abstract class RecipeDatabase : RoomDatabase()
{
    abstract fun recipeDao(): RecipeDao
    abstract fun favoriteDao(): FavoriteDao

    companion object
    {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE favorites ADD COLUMN name TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE favorites ADD COLUMN thumbnailUrl TEXT NOT NULL DEFAULT ''")
            }
        }

        fun getDatabase(context: Context): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "recipe_database"
                ).addMigrations(MIGRATION_1_2).build()
                INSTANCE = instance
                instance
            }
        }

    }
}
