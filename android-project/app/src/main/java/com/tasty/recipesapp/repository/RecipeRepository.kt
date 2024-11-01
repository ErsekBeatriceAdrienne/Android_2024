package com.tasty.recipesapp.repository

import android.content.Context
import com.tasty.recipesapp.models.InstructionModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class RecipeRepository(private val context: Context) {
    private val gson = Gson()

    fun getInstructions(): List<InstructionModel> {
        val jsonString = context.assets.open("instructions.json").bufferedReader().use { it.readText() }
        val instructionListType = object : TypeToken<List<InstructionModel>>() {}.type
        return gson.fromJson(jsonString, instructionListType)
    }
}