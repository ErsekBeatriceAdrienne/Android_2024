package com.tasty.recipesapp.mappings

import com.tasty.recipesapp.dtos.InstructionDTO
import com.tasty.recipesapp.models.InstructionModel

fun InstructionDTO.toModel(): InstructionModel
{
    return InstructionModel(
        id = this.instructionID,
        displayText = this.displayText
    )
}

fun List<InstructionDTO>.toModelList(): List<InstructionModel>
{
    return this.map { it.toModel() }
}
