package com.tasty.recipesapp.models.recipe
import com.google.gson.Gson
import com.tasty.recipesapp.database.entities.RecipeEntity
import com.tasty.recipesapp.models.recipe.recipemodels.ComponentModel
import com.tasty.recipesapp.models.recipe.recipemodels.InstructionModel
import com.tasty.recipesapp.models.recipe.recipemodels.NutritionModel

data class RecipeModel(
    var recipeID: Int,
    val name: String,
    val description: String,
    val thumbnailUrl: String,
    val keywords: String,
    val isPublic: Boolean,
    val userEmail: String,
    val originalVideoUrl: String,
    val country: String,
    val numServings: Int,
    val components: List<ComponentModel>,
    val instructions: List<InstructionModel>,
    val nutrition: NutritionModel
)

fun RecipeModel.toEntity(): RecipeEntity {
    val json = Gson().toJson(this)
    return RecipeEntity(json = json)
}

fun RecipeEntity.toModel(): RecipeModel {
    return Gson().fromJson(this.json, RecipeModel::class.java)
}
