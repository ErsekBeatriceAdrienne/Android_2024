package com.tasty.recipesapp.restapi.response

data class OAuthTokenResponse(
    val access_token: String,
    val id_token: String,
    val refresh_token: String?,
    val expires_in: Int,
    val token_type: String
)
