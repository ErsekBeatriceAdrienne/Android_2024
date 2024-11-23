package com.tasty.recipesapp.models.recipe
import com.google.gson.Gson
import com.tasty.recipesapp.database.entities.RecipeEntity
import com.tasty.recipesapp.models.recipe.recipemodels.ComponentModel
import com.tasty.recipesapp.models.recipe.recipemodels.InstructionModel
import com.tasty.recipesapp.models.recipe.recipemodels.NutritionModel

data class RecipeModel(
    var recipeID: Int,
    var name: String,
    var description: String,
    var thumbnailUrl: String,
    var keywords: String,
    var isPublic: Boolean,
    var userEmail: String,
    var originalVideoUrl: String,
    var country: String,
    var numServings: Int,
    var components: List<ComponentModel>,
    var instructions: List<InstructionModel>,
    var nutrition: NutritionModel,
    var isFavorite: Boolean
)

fun RecipeModel.toEntity(): RecipeEntity {
    val json = Gson().toJson(this)
    return RecipeEntity(json = json)
}

fun RecipeEntity.toModel(): RecipeModel {
    return Gson().fromJson(this.json, RecipeModel::class.java)
}
