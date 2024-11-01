package com.tasty.recipesapp.repository

import com.google.gson.Gson
import com.tasty.recipesapp.dtos.InstructionDTO
import com.tasty.recipesapp.models.InstructionModel

class RecipeRepository {

    private val dummyInstructions = listOf(
        InstructionModel(id = 1, displayText = "Chop the vegetables"),
        InstructionModel(id = 2, displayText = "Boil water"),
        InstructionModel(id = 3, displayText = "Add pasta to boiling water")
    )

    suspend fun getInstructions(): List<InstructionModel> {
        // Return dummy instructions for now
        return dummyInstructions
    }

    private fun parseJson(json: String): List<InstructionDTO> {
        // Parse JSON into InstructionDTO
        // You may need to implement this method to read from a JSON file
        return emptyList() // Placeholder
    }

    fun mapInstructionsToModels(instructions: List<InstructionDTO>): List<InstructionModel> {
        return instructions.map { InstructionModel(it.instructionID, it.displayText) }
    }
}
