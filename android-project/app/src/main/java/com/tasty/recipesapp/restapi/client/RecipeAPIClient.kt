package com.tasty.recipesapp.restapi.client

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.tasty.recipesapp.models.recipe.RecipeModel
import com.tasty.recipesapp.restapi.service.RecipeService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecipeAPIClient {
    private val client = OkHttpClient()

    private val BASE_URL = "https://recipe-appservice-cthjbdfafnhfdtes.germanywestcentral-01.azurewebsites.net"

    val apiService: RecipeService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RecipeService::class.java)

    suspend fun getRecipesFromApi(): List<RecipeModel> {
        return apiService.getRecipes()
    }
}
