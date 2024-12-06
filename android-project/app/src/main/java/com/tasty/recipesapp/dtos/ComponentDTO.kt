package com.tasty.recipesapp.dtos

import com.tasty.recipesapp.models.recipe.recipemodels.ComponentModel
import com.tasty.recipesapp.models.recipe.recipemodels.IngredientModel
import com.tasty.recipesapp.models.recipe.recipemodels.MeasurementModel

class ComponentDTO (
    val rawText: String,
    val ingredient: IngredientModel,
    val measurement: MeasurementModel,
    val position: Int
)

// Mapping function from DTO to Model
fun ComponentDTO.toModel(): ComponentModel
{
    return ComponentModel(
        rawText = this.rawText,
        ingredient = this.ingredient,
        measurement = this.measurement,
        position = this.position
    )
}

fun List<ComponentDTO>.toModelList(): List<ComponentModel> {
    return this.map { it.toModel() }
}
