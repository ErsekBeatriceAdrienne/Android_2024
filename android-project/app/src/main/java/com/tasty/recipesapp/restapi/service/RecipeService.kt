package com.tasty.recipesapp.restapi.service
import com.tasty.recipesapp.restapi.response.RecipeResponse
import retrofit2.http.GET

interface RecipeService {
    @GET("api/recipes")
    suspend fun getRecipes(): RecipeResponse
}
