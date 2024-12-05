package com.tasty.recipesapp.repository.restapi

import androidx.core.content.ContentProviderCompat.requireContext
import com.google.gson.Gson
import com.tasty.recipesapp.models.recipe.RecipeModel
import com.tasty.recipesapp.restapi.auth.SharedPrefsUtil
import com.tasty.recipesapp.restapi.service.RecipeService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecipeAPIRepository {
    private val client = OkHttpClient()

    // This function should be called within a coroutine scope
    /*suspend fun addRecipeToApi(recipe: RecipeModel): RecipeModel {
        val url = "https://recipe-appservice-cthjbdfafnhfdtes.germanywestcentral-01.azurewebsites.net/api/recipes"

        // Retrieve the access token properly
        val accessToken = SharedPrefsUtil.getAccessToken(requireContext())

        // Create request body and set headers
        val requestBody = Gson().toJson(recipe).toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        val response = client.newCall(request).execute()

        // Handle response
        return if (response.isSuccessful) {
            val responseBody = response.body?.string()
            Gson().fromJson(responseBody, RecipeModel::class.java)
        } else {
            throw Exception("Failed to add recipe: ${response.message}")
        }
    }*/
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
