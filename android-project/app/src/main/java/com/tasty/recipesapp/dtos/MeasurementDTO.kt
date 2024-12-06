package com.tasty.recipesapp.dtos
import com.tasty.recipesapp.models.recipe.recipemodels.MeasurementModel
import com.tasty.recipesapp.models.recipe.recipemodels.UnitModel

class MeasurementDTO (
    val quantity: String,
    val unit: UnitModel
)

fun MeasurementDTO.toModel(): MeasurementModel {
    return MeasurementModel(
        quantity = this.quantity,
        unit = this.unit
    )
}

fun List<MeasurementDTO>.toModelList(): List<MeasurementModel> {
    return this.map { it.toModel() }
}
