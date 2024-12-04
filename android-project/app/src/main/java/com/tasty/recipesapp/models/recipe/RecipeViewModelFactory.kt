package com.tasty.recipesapp.models.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tasty.recipesapp.repository.roomdatabase.LocalDBRepository

class RecipeViewModelFactory(private val recipeRepository: LocalDBRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            return RecipeViewModel(recipeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

