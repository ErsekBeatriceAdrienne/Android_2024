package com.tasty.recipesapp.models

data class RecipeModel(
    val recipeID: Int,
    val name: String,
    val description: String,
    val thumbnailUrl: String,
    val keywords: String,
    val isPublic: Boolean,
    val userEmail: String,
    val originalVideoUrl: String,
    val country: String,
    val numServings: Int,
    val components: List<String>,
    val instructions: List<String>,
    val nutrition: List<String>
)