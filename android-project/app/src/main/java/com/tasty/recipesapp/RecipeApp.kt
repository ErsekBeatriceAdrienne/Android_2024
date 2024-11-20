package com.tasty.recipesapp

import android.app.Application
import com.tasty.recipesapp.database.RecipeDatabase

class RecipeApp : Application()
{
    val database: RecipeDatabase by lazy {
        RecipeDatabase.getDatabase(this)
    }
}
