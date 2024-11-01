package com.tasty.recipesapp.models

data class RecipeModel(
    val recipeID: Int,
    val title: String,
    val instructions: List<InstructionModel>
)
