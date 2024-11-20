package com.tasty.recipesapp.database.provider

import android.content.Context
import com.tasty.recipesapp.database.RecipeDatabase
import com.tasty.recipesapp.database.dao.FavoriteDao
import com.tasty.recipesapp.database.dao.RecipeDao
import com.tasty.recipesapp.repository.LocalRepository

object RepositoryProvider {
    private lateinit var recipeDao: RecipeDao
    private lateinit var favoriteDao: FavoriteDao

    fun initialize(context: Context) {
        recipeDao = RecipeDatabase.getDatabase(context).recipeDao()
    }

    val localRepository: LocalRepository by lazy {
        checkInitialized()
        LocalRepository(recipeDao, favoriteDao)
    }

    private fun checkInitialized() {
        if (!::recipeDao.isInitialized) {
            throw UninitializedPropertyAccessException("RepositoryProvider has not been initialized")
        }
    }
}