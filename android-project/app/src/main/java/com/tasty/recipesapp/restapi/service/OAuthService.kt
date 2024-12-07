package com.tasty.recipesapp.restapi.service

import com.tasty.recipesapp.restapi.response.OAuthTokenResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface OAuthService {
    @FormUrlEncoded
    @POST("token")
    suspend fun exchangeAuthorizationCode(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") authorizationCode: String,
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("redirect_uri") redirectUri: String = "urn:ietf:wg:oauth:2.0:oob" // Update if necessary
    ): Response<OAuthTokenResponse>
}
