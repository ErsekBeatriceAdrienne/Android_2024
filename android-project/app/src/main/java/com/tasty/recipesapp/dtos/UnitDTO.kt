package com.tasty.recipesapp.dtos
import com.tasty.recipesapp.models.recipe.recipemodels.UnitModel

class UnitDTO  (
    val name: String,
    val displaySingular: String,
    val displayPlural: String,
    val abbreviation: String
)

fun UnitDTO.toModel(): UnitModel {
    return UnitModel(
        name = this.name,
        displaySingular = this.displaySingular,
        displayPlural = this.displayPlural,
        abbreviation = this.abbreviation
    )
}

fun List<UnitDTO>.toModelList(): List<UnitModel> {
    return this.map { it.toModel() }
}
