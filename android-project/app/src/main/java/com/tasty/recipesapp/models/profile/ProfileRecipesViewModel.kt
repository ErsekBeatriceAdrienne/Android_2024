package com.tasty.recipesapp.models.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasty.recipesapp.models.recipe.RecipeModel
import com.tasty.recipesapp.repository.LocalRepository
import kotlinx.coroutines.launch

class ProfileRecipesViewModel(private val repository: LocalRepository) : ViewModel() {

    private val _recipes = MutableLiveData<List<RecipeModel>>()
    val recipes: LiveData<List<RecipeModel>> get() = _recipes

    init {
        loadAllRecipes()
    }

    private fun loadAllRecipes() {
        viewModelScope.launch {
            _recipes.value = repository.getAllRecipes()
        }
    }

    fun insertRecipe(recipe: RecipeModel) {
        viewModelScope.launch {
            repository.insertRecipe(recipe)
            loadAllRecipes()
        }
    }

    fun deleteRecipe(recipe: RecipeModel) {
        viewModelScope.launch {
            repository.deleteRecipe(recipe)
            loadAllRecipes()
        }
    }
}