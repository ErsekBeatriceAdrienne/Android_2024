package com.tasty.recipesapp.models.recipe.recipemodels

import com.tasty.recipesapp.restapi.response.Ingredient
import com.tasty.recipesapp.restapi.response.Measurement

data class ComponentModel(
    val rawText: String,
    val ingredient: IngredientModel,
    val measurement: MeasurementModel,
    val position: Int
)
