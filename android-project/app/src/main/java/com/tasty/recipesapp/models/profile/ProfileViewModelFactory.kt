package com.tasty.recipesapp.models.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tasty.recipesapp.repository.roomdatabase.LocalDBRepository

class ProfileViewModelFactory(private val repository: LocalDBRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileRecipesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileRecipesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}