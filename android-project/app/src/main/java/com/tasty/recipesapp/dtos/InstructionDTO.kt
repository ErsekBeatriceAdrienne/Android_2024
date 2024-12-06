package com.tasty.recipesapp.dtos

import com.tasty.recipesapp.models.recipe.recipemodels.InstructionModel

data class InstructionDTO(
    val instructionID: Int,
    val displayText: String,
    val position: Int
)

// Mapping function from DTO to Model
fun InstructionDTO.toModel(): InstructionModel
{
    return InstructionModel(
        instructionID = this.instructionID,
        displayText = this.displayText,
        position = this.position
    )
}

fun List<InstructionDTO>.toModelList(): List<InstructionModel> {
    return this.map { it.toModel() }
}
