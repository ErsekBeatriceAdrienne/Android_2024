package com.tasty.recipesapp.ui.addrecipeapi

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tasty.recipesapp.R
import com.tasty.recipesapp.models.recipe.RecipeModel
import com.tasty.recipesapp.models.recipe.recipemodels.ComponentModel
import com.tasty.recipesapp.models.recipe.recipemodels.IngredientModel
import com.tasty.recipesapp.models.recipe.recipemodels.InstructionModel
import com.tasty.recipesapp.models.recipe.recipemodels.MeasurementModel
import com.tasty.recipesapp.models.recipe.recipemodels.NutritionModel
import com.tasty.recipesapp.models.recipe.recipemodels.UnitModel
import com.tasty.recipesapp.restapi.client.RecipeAPIClient
import com.tasty.recipesapp.restapi.auth.SharedPrefsUtil
import com.tasty.recipesapp.restapi.auth.TokenProvider
import com.tasty.recipesapp.restapi.service.RecipeService
import kotlinx.coroutines.launch

class AddNewRecipeAPIFragment : Fragment() {
    private val recipeAPIClient = RecipeAPIClient()

    private lateinit var recipeTitleEditText: EditText
    private lateinit var recipeDescriptionEditText: EditText
    private lateinit var thumbnailUrlEditText: EditText
    private lateinit var keywordsEditText: EditText
    private lateinit var isPublicCheckbox: CheckBox
    private lateinit var originalVideoUrlEditText: EditText
    private lateinit var countryEditText: EditText
    private lateinit var numServingsEditText: EditText
    private lateinit var caloriesEditText: EditText
    private lateinit var fatEditText: EditText
    private lateinit var proteinEditText: EditText
    private lateinit var sugarEditText: EditText
    private lateinit var carbsEditText: EditText
    private lateinit var fiberEditText: EditText
    private lateinit var dynamicFieldsLayout: LinearLayout
    private lateinit var addComponentButton: Button
    private lateinit var addRecipeButton: Button
    private lateinit var instructionsContainer: LinearLayout
    private lateinit var addInstructionButton: Button
    private val instructionsList = mutableListOf<InstructionModel>()
    private var instructionCounter = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_new_recipe, container, false)

        // Initialize all the EditTexts
        recipeTitleEditText = view.findViewById(R.id.recipeTitleEditText)
        recipeDescriptionEditText = view.findViewById(R.id.recipeDescriptionEditText)
        thumbnailUrlEditText = view.findViewById(R.id.thumbnailUrlEditText)
        keywordsEditText = view.findViewById(R.id.keywordsEditText)
        isPublicCheckbox= view.findViewById(R.id.isPublicCheckBox)
        originalVideoUrlEditText = view.findViewById(R.id.originalVideoUrlEditText)
        countryEditText = view.findViewById(R.id.countryEditText)
        numServingsEditText = view.findViewById(R.id.numServingsEditText)
        caloriesEditText = view.findViewById(R.id.caloriesEditText)
        fatEditText = view.findViewById(R.id.fatEditText)
        proteinEditText = view.findViewById(R.id.proteinEditText)
        sugarEditText = view.findViewById(R.id.sugarEditText)
        carbsEditText = view.findViewById(R.id.carbsEditText)
        fiberEditText = view.findViewById(R.id.fiberEditText)
        addRecipeButton = view.findViewById(R.id.addRecipeButton)
        dynamicFieldsLayout = view.findViewById(R.id.dynamicFieldsLayout)
        addComponentButton = view.findViewById(R.id.addComponentButton)
        instructionsContainer = view.findViewById(R.id.instructionsContainer)
        addInstructionButton = view.findViewById(R.id.addInstructionButton)

        // Set up the Add Instruction Button
        addInstructionButton.setOnClickListener {
            addInstructionField()
        }

        addComponentButton.setOnClickListener {
            addDynamicFields()
        }

        addRecipeButton.setOnClickListener {
            // Check if the user is logged in
            if (SharedPrefsUtil.isLoggedIn(requireContext())) {
                lifecycleScope.launch {
                    addRecipe()
                }
            } else {
                // If not logged in, prompt to log in
                Toast.makeText(requireContext(), "Please log in to create a recipe.", Toast.LENGTH_SHORT).show()
                // Optionally, navigate to a login screen
                //showLoginPromptDialog()
            }
        }

        return view
    }

    // Method to dynamically add a new instruction field
    private fun addInstructionField() {
        val instructionEditText = EditText(requireContext()).apply {
            hint = "Enter instruction"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8, 0, 8)
            }
        }

        // Add the EditText to the container
        instructionsContainer.addView(instructionEditText)
    }


    // Method to save the instruction to the list and update the UI
    private fun saveInstruction(instructionText: String) {
        val instructionModel = InstructionModel(
            instructionID = instructionCounter++,
            displayText = instructionText,
            position = instructionsList.size + 1
        )
        instructionsList.add(instructionModel)
        Toast.makeText(requireContext(), "Instruction added", Toast.LENGTH_SHORT).show()
    }

    private fun addDynamicFields() {
        // Create the three EditTexts for Ingredient, Quantity, and Unit
        val ingredientNameEditText = EditText(requireContext()).apply {
            hint = "Ingredient Name"
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val quantityEditText = EditText(requireContext()).apply {
            hint = "Quantity"
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val unitEditText = EditText(requireContext()).apply {
            hint = "Unit"
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        // Add the three fields to the dynamic layout
        val horizontalLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            addView(ingredientNameEditText)
            addView(quantityEditText)
            addView(unitEditText)
        }

        dynamicFieldsLayout.addView(horizontalLayout)
        Log.d("AddNewRecipeFragment", "Dynamic fields added.")
    }


    private suspend fun addRecipe() {
        // Get the user input from the UI
        val title = recipeTitleEditText.text.toString().trim()
        val description = recipeDescriptionEditText.text.toString().trim()

        // Validate inputs
        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(requireContext(), "Title and description are required!", Toast.LENGTH_SHORT).show()
            return
        }

        // Create RecipeModel
        val recipe = RecipeModel(
            name = title,
            description = description,
            thumbnailUrl = thumbnailUrlEditText.text.toString(),
            keywords = keywordsEditText.text.toString(),
            isPublic = isPublicCheckbox.isChecked,
            originalVideoUrl = originalVideoUrlEditText.text.toString(),
            country = countryEditText.text.toString(),
            numServings = numServingsEditText.text.toString().toIntOrNull() ?: 0,
            components = collectComponents(),
            instructions = collectInstructions(),
            nutrition = NutritionModel(
                calories = caloriesEditText.text.toString().toIntOrNull() ?: 0,
                fat = fatEditText.text.toString().toIntOrNull() ?: 0,
                protein = proteinEditText.text.toString().toIntOrNull() ?: 0,
                sugar = sugarEditText.text.toString().toIntOrNull() ?: 0,
                carbohydrates = carbsEditText.text.toString().toIntOrNull() ?: 0,
                fiber = fiberEditText.text.toString().toIntOrNull() ?: 0
            )
        )

        // Show loading indicator (e.g., a progress bar)
        showLoading(true)

        try {
            // Post the recipe data to API
            //val addedRecipe = recipeAPIRepository.addRecipeToApi(recipe)
            Toast.makeText(requireContext(), "Recipe added successfully!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error adding recipe: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            // Hide loading indicator
            showLoading(false)
        }
    }

    private fun collectComponents(): List<ComponentModel> {
        val components = mutableListOf<ComponentModel>()
        // Loop through dynamic fields and collect data for components
        for (i in 0 until dynamicFieldsLayout.childCount) {
            val dynamicRow = dynamicFieldsLayout.getChildAt(i) as LinearLayout
            val ingredientEditText = dynamicRow.getChildAt(0) as EditText
            val quantityEditText = dynamicRow.getChildAt(1) as EditText
            val unitEditText = dynamicRow.getChildAt(2) as EditText

            val ingredientName = ingredientEditText.text.toString().trim()
            val quantity = quantityEditText.text.toString().trim()
            val unitName = unitEditText.text.toString().trim()

            if (ingredientName.isNotEmpty() && quantity.isNotEmpty() && unitName.isNotEmpty()) {
                val ingredient = IngredientModel(name = ingredientName)
                val unit = UnitModel(name = unitName, displaySingular = unitName, displayPlural = unitName + "s", abbreviation = unitName.take(2))
                val measurement = MeasurementModel(quantity = quantity, unit = unit)

                val component = ComponentModel(
                    rawText = "$ingredientName:$quantity $unitName",
                    ingredient = ingredient,
                    measurement = measurement,
                    position = i + 1 // Position starts from 1
                )
                components.add(component)
            }
        }
        return components
    }

    private fun collectInstructions(): List<InstructionModel> {
        val instructions = mutableListOf<InstructionModel>()
        for (i in 0 until instructionsContainer.childCount) {
            val instructionEditText = instructionsContainer.getChildAt(i) as EditText
            val instructionText = instructionEditText.text.toString().trim()
            if (instructionText.isNotEmpty()) {
                val instruction = InstructionModel(
                    instructionID = instructionCounter++,
                    displayText = instructionText,
                    position = instructions.size + 1
                )
                instructions.add(instruction)
            }
        }
        return instructions
    }

    private fun showLoading(isLoading: Boolean) {
        addRecipeButton.isEnabled = !isLoading
        if (isLoading) {
            // Show a loading spinner or something similar
        } else {
            // Hide the loading spinner
        }
    }

    suspend fun addRecipe(recipe : RecipeModel) {
        try {
            val response = recipeAPIClient.apiService.addRecipe(recipe)
            if (response.isSuccessful) {
                Toast.makeText(requireContext(), "Recipe added successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Failed to add recipe: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error adding recipe", e)
        }
    }

    private fun checkLoginAndAddRecipe() {
        val token = TokenProvider(requireContext()).getAuthToken()
        if (token.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please log in to add a recipe.", Toast.LENGTH_SHORT).show()
            //signInWithGoogle()
        } else {
            lifecycleScope.launch {
                addRecipe()
            }
        }
    }

}
