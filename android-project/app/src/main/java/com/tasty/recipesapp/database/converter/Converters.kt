package com.tasty.recipesapp.database.converter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tasty.recipesapp.models.recipe.recipemodels.ComponentModel
import com.tasty.recipesapp.models.recipe.recipemodels.InstructionModel
import com.tasty.recipesapp.models.recipe.recipemodels.NutritionModel

class Converters {
    private val gson = Gson()

    // Convert List<ComponentModel> to JSON string
    @TypeConverter
    fun fromComponentList(components: List<ComponentModel>): String {
        return gson.toJson(components)
    }

    // Convert JSON string to List<ComponentModel>
    @TypeConverter
    fun toComponentList(data: String): List<ComponentModel> {
        val type = object : TypeToken<List<ComponentModel>>() {}.type
        return gson.fromJson(data, type)
    }

    // Convert List<InstructionModel> to JSON string
    @TypeConverter
    fun fromInstructionList(instructions: List<InstructionModel>): String {
        return gson.toJson(instructions)
    }

    // Convert JSON string to List<InstructionModel>
    @TypeConverter
    fun toInstructionList(data: String): List<InstructionModel> {
        val type = object : TypeToken<List<InstructionModel>>() {}.type
        return gson.fromJson(data, type)
    }

    // Convert NutritionModel to JSON string
    @TypeConverter
    fun fromNutritionModel(nutrition: NutritionModel): String {
        return gson.toJson(nutrition)
    }

    // Convert JSON string to NutritionModel
    @TypeConverter
    fun toNutritionModel(data: String): NutritionModel {
        return gson.fromJson(data, NutritionModel::class.java)
    }
}
