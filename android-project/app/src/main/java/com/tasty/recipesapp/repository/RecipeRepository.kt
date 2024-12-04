package com.tasty.recipesapp.repository

import com.tasty.recipesapp.models.recipe.RecipeModel
import com.tasty.recipesapp.restapi.service.RecipeService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecipeRepository {

    private val baseUrl = "https://recipe-appservice-cthjbdfafnhfdtes.germanywestcentral-01.azurewebsites.net"

    private val apiService: RecipeService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RecipeService::class.java)

    suspend fun getRecipesFromApi(): List<RecipeModel> {
        return apiService.getRecipes()
    }
}

