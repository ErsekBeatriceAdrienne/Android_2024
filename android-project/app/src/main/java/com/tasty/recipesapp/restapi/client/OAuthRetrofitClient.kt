package com.tasty.recipesapp.restapi.client

import com.tasty.recipesapp.restapi.service.OAuthService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OAuthRetrofitClient {
    private const val BASE_URL = "https://oauth2.googleapis.com/"

    val api: OAuthService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OAuthService::class.java)
    }
}