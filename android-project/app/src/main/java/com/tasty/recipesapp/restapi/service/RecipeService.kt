package com.tasty.recipesapp.restapi.service

import com.tasty.recipesapp.models.recipe.RecipeModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RecipeService {
    @GET("api/recipes")
    suspend fun getRecipes(): List<RecipeModel>
    @POST("api/recipes/add")
    suspend fun addRecipe(@Body recipe: RecipeModel): Response<RecipeModel>
}
