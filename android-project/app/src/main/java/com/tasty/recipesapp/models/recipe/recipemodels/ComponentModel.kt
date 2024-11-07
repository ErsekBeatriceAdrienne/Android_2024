package com.tasty.recipesapp.models.recipe.recipemodels

data class ComponentModel (
    val rawText: String,
    val ingredient: IngredientModel,
    val measurement: MeasurementModel,
    val position: Int
)