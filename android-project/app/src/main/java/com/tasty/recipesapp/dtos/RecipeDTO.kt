package com.tasty.recipesapp.dtos

import com.tasty.recipesapp.models.recipe.RecipeModel

data class RecipeDTO(
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
    val components: List<ComponentDTO>,
    val instructions: List<InstructionDTO>,
    val nutrition: NutritionDTO
)

// Mapping function from RecipeDTO to RecipeModel
fun RecipeDTO.toModel(): RecipeModel {
    return RecipeModel(
        recipeID = this.recipeID,
        name = this.name,
        description = this.description,
        thumbnailUrl = this.thumbnailUrl,
        keywords = this.keywords,
        isPublic = this.isPublic,
        userEmail = this.userEmail,
        originalVideoUrl = this.originalVideoUrl,
        country = this.country,
        numServings = this.numServings,
        components = this.components.toModelList(),
        instructions = this.instructions.toModelList(),
        nutrition = this.nutrition.toModel()
    )
}