package com.tasty.recipesapp.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tasty.recipesapp.repository.ProfileRepository

class ProfileViewModelFactory(private val recipeRepository: ProfileRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileRecipesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileRecipesViewModel(recipeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}