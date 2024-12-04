package com.tasty.recipesapp.restapi.response

import com.tasty.recipesapp.models.recipe.RecipeModel

data class RecipeResponse(
    val recipes: List<RecipeModel>
)
