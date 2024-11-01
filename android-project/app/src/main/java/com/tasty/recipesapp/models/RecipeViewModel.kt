package com.tasty.recipesapp.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tasty.recipesapp.repository.RecipeRepository

class RecipeViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {
    private val _recipes = MutableLiveData<List<RecipeModel>>()
    val recipes: LiveData<List<RecipeModel>> get() = _recipes

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        _recipes.value = recipeRepository.getRecipes()
    }
}