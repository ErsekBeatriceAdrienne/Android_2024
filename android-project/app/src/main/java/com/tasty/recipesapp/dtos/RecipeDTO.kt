package com.tasty.recipesapp.dtos

data class RecipeDTO(
    val recipeID: Int,
    val title: String,
    val instructions: List<InstructionDTO>
)
