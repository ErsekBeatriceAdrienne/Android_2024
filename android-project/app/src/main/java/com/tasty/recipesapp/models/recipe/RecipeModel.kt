package com.tasty.recipesapp.models.recipe
import com.google.gson.Gson
import com.tasty.recipesapp.database.entities.RecipeEntity
import com.tasty.recipesapp.models.recipe.recipemodels.ComponentModel
import com.tasty.recipesapp.models.recipe.recipemodels.InstructionModel
import com.tasty.recipesapp.models.recipe.recipemodels.NutritionModel

data class RecipeModel(
    var recipeID: Int = 0,
    var name: String = "",
    var description: String = "",
    var thumbnailUrl: String = "",
    var keywords: String = "",
    var isPublic: Boolean = false,
    var userEmail: String = "",
    var originalVideoUrl: String = "",
    var country: String = "",
    var numServings: Int = 0,
    var components: List<ComponentModel> = emptyList(),
    var instructions: List<InstructionModel> = emptyList(),
    var nutrition: NutritionModel = NutritionModel(0,0,0,0,0,0),
    var isFavorite: Boolean = false
)

fun RecipeModel.toEntity(): RecipeEntity {
    val json = Gson().toJson(this)
    return RecipeEntity(json = json)
}

fun RecipeEntity.toModel(): RecipeModel {
    return Gson().fromJson(this.json, RecipeModel::class.java)
}
