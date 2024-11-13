package com.tasty.recipesapp.ui.addrecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tasty.recipesapp.R
import com.tasty.recipesapp.database.RecipeDatabase
import com.tasty.recipesapp.database.dao.RecipeDao
import com.tasty.recipesapp.models.recipe.RecipeModel
import com.tasty.recipesapp.models.recipe.recipemodels.ComponentModel
import com.tasty.recipesapp.models.recipe.recipemodels.IngredientModel
import com.tasty.recipesapp.models.recipe.recipemodels.InstructionModel
import com.tasty.recipesapp.models.recipe.recipemodels.MeasurementModel
import com.tasty.recipesapp.models.recipe.recipemodels.NutritionModel
import com.tasty.recipesapp.models.recipe.recipemodels.UnitModel
import com.tasty.recipesapp.repository.LocalRepository
import com.tasty.recipesapp.repository.RecipeRepository
import kotlinx.coroutines.launch

class AddNewRecipeFragment : Fragment()
{
    private lateinit var recipeTitleEditText: EditText
    private lateinit var recipeDescriptionEditText: EditText
    private lateinit var thumbnailUrlEditText: EditText
    private lateinit var keywordsEditText: EditText
    private lateinit var isPublicEditText: EditText
    private lateinit var userEmailEditText: EditText
    private lateinit var originalVideoUrlEditText: EditText
    private lateinit var countryEditText: EditText
    private lateinit var numServingsEditText: EditText
    private lateinit var componentsEditText: EditText
    private lateinit var instructionsEditText: EditText
    private lateinit var nutritionEditText: EditText
    private lateinit var addRecipeButton: Button
    private lateinit var repository: LocalRepository
    private lateinit var recipeDao: RecipeDao

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val db = RecipeDatabase.getDatabase(requireContext())
        recipeDao = db.recipeDao()
        repository = LocalRepository(recipeDao)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.fragment_add_new_recipe, container, false)

        recipeTitleEditText = view.findViewById(R.id.recipeTitleEditText)
        recipeDescriptionEditText = view.findViewById(R.id.recipeDescriptionEditText)
        thumbnailUrlEditText = view.findViewById(R.id.thumbnailUrlEditText)
        keywordsEditText = view.findViewById(R.id.keywordsEditText)
        isPublicEditText = view.findViewById(R.id.isPublicEditText)
        userEmailEditText = view.findViewById(R.id.userEmailEditText)
        originalVideoUrlEditText = view.findViewById(R.id.originalVideoUrlEditText)
        countryEditText = view.findViewById(R.id.countryEditText)
        numServingsEditText = view.findViewById(R.id.numServingsEditText)
        componentsEditText = view.findViewById(R.id.componentsEditText)
        instructionsEditText = view.findViewById(R.id.instructionsEditText)
        nutritionEditText = view.findViewById(R.id.nutritionEditText)
        addRecipeButton = view.findViewById(R.id.addRecipeButton)

        addRecipeButton.setOnClickListener {
            // Call the method to add the recipe
            lifecycleScope.launch {
                addRecipe()
            }
        }

        return view
    }

    private suspend fun addRecipe()
    {
        val title = recipeTitleEditText.text.toString()
        val description = recipeDescriptionEditText.text.toString()

        if (title.isEmpty() || description.isEmpty())
        {
            Toast.makeText(requireContext(), "Title and description are required!", Toast.LENGTH_SHORT).show()
            return
        }

        val thumbnailUrl = thumbnailUrlEditText.text.toString()
        val keywords = keywordsEditText.text.toString()
        val isPublic = isPublicEditText.text.toString().toBoolean()
        val userEmail = userEmailEditText.text.toString()
        val originalVideoUrl = originalVideoUrlEditText.text.toString()
        val country = countryEditText.text.toString()
        val numServings = numServingsEditText.text.toString().toIntOrNull() ?: 0
        // Convert components
        val components = componentsEditText.text.toString()
            .split(",")
            .mapIndexed { index, text ->
                val parts = text.trim().split(":") // Assuming "ingredient:measurement" format
                if (parts.size == 2) {
                    val ingredient = IngredientModel(name = parts[0].trim())
                    val measurementParts = parts[1].trim().split(" ") // Assuming "quantity unit" format
                    val measurement = if (measurementParts.size >= 2) {
                        val quantity = measurementParts[0]
                        val unit = UnitModel(
                            name = measurementParts[1], // Simplified, adjust as necessary
                            displaySingular = measurementParts[1],
                            displayPlural = measurementParts[1] + "s",  // Adding plural form (can adjust)
                            abbreviation = measurementParts[1].take(2) // Simplified abbreviation
                        )
                        MeasurementModel(quantity = quantity, unit = unit)
                    } else {
                        // Handle cases where measurement is missing or malformed
                        MeasurementModel(quantity = "", unit = UnitModel("", "", "", ""))
                    }

                    ComponentModel(
                        rawText = text.trim(),
                        ingredient = ingredient,
                        measurement = measurement,
                        position = index + 1 // Position starts from 1
                    )
                } else {
                    val ingredient = IngredientModel(name = parts[0].trim())
                    val measurement = MeasurementModel(quantity = "", unit = UnitModel("", "", "", ""))

                    ComponentModel(
                        rawText = text.trim(),
                        ingredient = ingredient,
                        measurement = measurement,
                        position = index + 1
                    )
                }
            }

        // Convert instructions
        val instructions = instructionsEditText.text.toString()
            .split(",")
            .mapIndexed { index, instruction ->
                InstructionModel(
                    instructionID = index + 1, // You can use a simple index or fetch an ID if needed
                    displayText = instruction.trim(),
                    position = index + 1
                )
            }

        // Convert nutrition
        val nutrition = nutritionEditText.text.toString()
            .split(",")
            .mapNotNull {
                val parts = it.trim().split(":")
                if (parts.size == 2) {
                    val nutrient = parts[0].trim()
                    val amount = parts[1].trim().toIntOrNull() ?: 0
                    when (nutrient.lowercase()) {
                        "calories" -> NutritionModel(calories = amount, protein = 0, fat = 0, carbohydrates = 0, sugar = 0, fiber = 0)
                        "protein" -> NutritionModel(calories = 0, protein = amount, fat = 0, carbohydrates = 0, sugar = 0, fiber = 0)
                        "fat" -> NutritionModel(calories = 0, protein = 0, fat = amount, carbohydrates = 0, sugar = 0, fiber = 0)
                        "carbohydrates" -> NutritionModel(calories = 0, protein = 0, fat = 0, carbohydrates = amount, sugar = 0, fiber = 0)
                        "sugar" -> NutritionModel(calories = 0, protein = 0, fat = 0, carbohydrates = 0, sugar = amount, fiber = 0)
                        "fiber" -> NutritionModel(calories = 0, protein = 0, fat = 0, carbohydrates = 0, sugar = 0, fiber = amount)
                        else -> null
                    }
                } else {
                    null
                }
            }.firstOrNull() ?: NutritionModel(calories = 0, protein = 0, fat = 0, carbohydrates = 0, sugar = 0, fiber = 0)


        val existingRecipes = repository.getAllRecipes()
        val newRecipeId = if (existingRecipes.isNotEmpty()) {
            existingRecipes.maxOf { it.recipeID } + 1
        } else 1

        val newRecipe = RecipeModel(
            recipeID = newRecipeId,
            name = title,
            description = description,
            thumbnailUrl = thumbnailUrl,
            keywords = keywords,
            isPublic = isPublic,
            userEmail = userEmail,
            originalVideoUrl = originalVideoUrl,
            country = country,
            numServings = numServings,
            components = components,
            instructions = instructions,
            nutrition = nutrition
        )

        // Insert into Room database
        repository.insertRecipe(newRecipe)

        Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_SHORT).show()
    }
}