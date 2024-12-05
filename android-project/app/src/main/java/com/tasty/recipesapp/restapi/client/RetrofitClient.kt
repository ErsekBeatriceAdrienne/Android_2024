package com.tasty.recipesapp.restapi.client

import android.content.Context
import com.tasty.recipesapp.restapi.auth.AuthInterceptor
import com.tasty.recipesapp.restapi.auth.TokenProvider
import com.tasty.recipesapp.restapi.service.RecipeService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient(context: Context) {

    private val tokenProvider = TokenProvider(context)
    private val authInterceptor = AuthInterceptor(tokenProvider)

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor) // Add the auth interceptor to every request
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://recipe-appservice-cthjbdfafnhfdtes.germanywestcentral-01.azurewebsites.net")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient) // Set the OkHttp client for authentication
        .build()

    fun getRecipeService(): RecipeService {
        return retrofit.create(RecipeService::class.java)
    }
}