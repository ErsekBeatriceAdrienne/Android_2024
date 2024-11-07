package com.tasty.recipesapp.dtos

import com.tasty.recipesapp.models.recipe.recipemodels.NutritionModel

data class NutritionDTO(
    val calories: Int,
    val protein: Int,
    val fat: Int,
    val carbohydrates: Int,
    val sugar: Int,
    val fiber: Int
)

// Mapping functions for NutritionDTO to NutritionModel
fun NutritionDTO.toModel(): NutritionModel {
    return NutritionModel(
        calories = this.calories,
        protein = this.protein,
        fat = this.fat,
        carbohydrates = this.carbohydrates,
        sugar = this.sugar,
        fiber = this.fiber
    )
}

fun List<NutritionDTO>.toModelList(): List<NutritionModel> {
    return this.map { it.toModel() }
}
