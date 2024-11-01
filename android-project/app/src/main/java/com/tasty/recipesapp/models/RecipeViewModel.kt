package com.tasty.recipesapp.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tasty.recipesapp.repository.RecipeRepository

class RecipeViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {
    private val _instructions = MutableLiveData<List<InstructionModel>>()
    val instructions: LiveData<List<InstructionModel>> get() = _instructions

    init {
        loadInstructions()
    }

    private fun loadInstructions() {
        _instructions.value = recipeRepository.getInstructions()
    }
}