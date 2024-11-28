package com.tasty.recipesapp.restapi.client

import com.tasty.recipesapp.restapi.response.RecipeResponse
import com.tasty.recipesapp.restapi.service.RecipeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecipeApiClient {
    companion object {
        private const val BASE_URL = "https://recipe-appservice-cthjbdfafnhfdtes.germanywestcentral-01.azurewebsites.net/"
    }

    private val recipeService: RecipeService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        recipeService = retrofit.create(RecipeService::class.java)
    }

    suspend fun getRecipes(): RecipeResponse? {
        return withContext(Dispatchers.IO) {
            try {
                recipeService.getRecipes()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
