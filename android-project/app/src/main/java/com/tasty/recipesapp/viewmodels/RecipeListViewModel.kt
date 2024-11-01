package com.tasty.recipesapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasty.recipesapp.models.InstructionModel
import com.tasty.recipesapp.repository.RecipeRepository
import kotlinx.coroutines.launch

class RecipeListViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _instructions = MutableLiveData<List<InstructionModel>>()
    val instructions: LiveData<List<InstructionModel>> get() = _instructions

    fun fetchInstructions() {
        viewModelScope.launch {
            // Fetch the instructions from the repository
            val instructionModels = repository.getInstructions()
            _instructions.value = instructionModels
        }
    }
}
