package com.tasty.recipesapp.restapi.response

import com.tasty.recipesapp.models.recipe.recipemodels.ComponentModel
import com.tasty.recipesapp.models.recipe.recipemodels.IngredientModel
import com.tasty.recipesapp.models.recipe.recipemodels.InstructionModel
import com.tasty.recipesapp.models.recipe.recipemodels.MeasurementModel
import com.tasty.recipesapp.models.recipe.recipemodels.NutritionModel
import com.tasty.recipesapp.models.recipe.recipemodels.UnitModel

data class RecipeResponse(
    val recipes: List<Recipe>
)

data class Recipe(
    val recipeID: Int,
    val name: String,
    val description: String,
    val thumbnailUrl: String,
    val keywords: String,
    val isPublic: Boolean,
    val userEmail: String,
    val originalVideoUrl: String?,
    val country: String,
    val numServings: Int,
    val components: List<Component>,
    val instructions: List<Instruction>,
    val nutrition: Nutrition,
    val isFavorite: Boolean
)

data class Component(
    val rawText: String,
    val ingredient: Ingredient,
    val measurement: Measurement,
    val position: Int
)


fun Component.toModel(): ComponentModel {
    return ComponentModel(
        // Map fields from `Component` to `ComponentModel`
        rawText = this.rawText,
        ingredient = this.ingredient.toModel(),
        measurement = this.measurement.toModel(),
        position = this.position
    )
}

data class Ingredient(
    val name: String
)

fun Ingredient.toModel(): IngredientModel {
    return IngredientModel(
        name = name
    )
}

data class Instruction(
    val instructionID: Int,
    val displayText: String,
    val position: Int
)

fun Instruction.toModel(): InstructionModel {
    return InstructionModel(
        instructionID = this.instructionID,
        displayText = this.displayText,
        position = this.position
    )
}

data class Nutrition(
    val calories: Int,
    val protein: Int,
    val fat: Int,
    val carbohydrates: Int,
    val sugar: Int,
    val fiber: Int
)

fun Nutrition.toModel(): NutritionModel {
    return NutritionModel(
        calories = this.calories,
        protein = this.protein,
        fat = this.fat,
        carbohydrates = this.carbohydrates,
        sugar = this.sugar,
        fiber = this.fiber
    )
}

data class Measurement (
    val quantity: String,
    val unit: Unit
)

fun Measurement.toModel(): MeasurementModel {
    return MeasurementModel(
        quantity = this.quantity,
        unit = this.unit.toModel()
    )
}

data class Unit (
    val name: String,
    val displaySingular: String,
    val displayPlural: String,
    val abbreviation: String
)

fun Unit.toModel(): UnitModel {
    return UnitModel(
        name = this.name,
        displaySingular = this.displaySingular,
        displayPlural = this.displayPlural,
        abbreviation = this.abbreviation
    )
}

