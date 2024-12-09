package com.tasty.recipesapp.models.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.tasty.recipesapp.models.recipe.RecipeModel
import com.tasty.recipesapp.models.recipe.toEntity
import com.tasty.recipesapp.repository.roomdatabase.LocalDBRepository
import kotlinx.coroutines.launch

class ProfileRecipesViewModel(private val repository: LocalDBRepository) : ViewModel() {

    private val _recipes = MutableLiveData<List<RecipeModel>>()
    val recipes: LiveData<List<RecipeModel>> get() = _recipes

    private val _randomRecipe = MutableLiveData<RecipeModel?>()
    val randomRecipe: LiveData<RecipeModel?> get() = _randomRecipe

    init {
        loadAllRecipes()
    }

    private fun loadAllRecipes() {
        viewModelScope.launch {
            val recipes = repository.getAllRecipes() ?: emptyList()
            _recipes.postValue(recipes)
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
            val entity = recipe.toEntity()
            repository.deleteRecipe(entity)
            loadAllRecipes()
        }
    }

    fun getRandomRecipe() {
        _randomRecipe.value = _recipes.value?.shuffled()?.firstOrNull()
    }

    fun getGoogleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("YOUR_GOOGLE_CLIENT_ID") // Replace with your Google client ID
            .requestEmail()
            .build()
    }
}