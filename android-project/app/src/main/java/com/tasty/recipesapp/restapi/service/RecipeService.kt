package com.tasty.recipesapp.restapi.service

import com.tasty.recipesapp.models.recipe.RecipeModel
import retrofit2.http.GET

interface RecipeService {
    @GET("api/recipes")
    suspend fun getRecipes(): List<RecipeModel>
}
