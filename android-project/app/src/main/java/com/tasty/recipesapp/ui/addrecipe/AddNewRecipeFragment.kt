package com.tasty.recipesapp.ui.addrecipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tasty.recipesapp.R
import com.tasty.recipesapp.database.RecipeDatabase
import com.tasty.recipesapp.database.dao.FavoriteDao
import com.tasty.recipesapp.database.dao.RecipeDao
import com.tasty.recipesapp.models.recipe.RecipeModel
import com.tasty.recipesapp.models.recipe.recipemodels.ComponentModel
import com.tasty.recipesapp.models.recipe.recipemodels.IngredientModel
import com.tasty.recipesapp.models.recipe.recipemodels.InstructionModel
import com.tasty.recipesapp.models.recipe.recipemodels.MeasurementModel
import com.tasty.recipesapp.models.recipe.recipemodels.NutritionModel
import com.tasty.recipesapp.models.recipe.recipemodels.UnitModel
import com.tasty.recipesapp.repository.LocalRepository
import kotlinx.coroutines.launch

class AddNewRecipeFragment : Fragment() {

    private lateinit var recipeTitleEditText: EditText
    private lateinit var recipeDescriptionEditText: EditText
    private lateinit var thumbnailUrlEditText: EditText
    private lateinit var keywordsEditText: EditText
    private lateinit var isPublicCheckbox: CheckBox
    private lateinit var userEmailEditText: EditText
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
    private lateinit var repository: LocalRepository
    private lateinit var recipeDao: RecipeDao
    private lateinit var favoriteDao: FavoriteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = RecipeDatabase.getDatabase(requireContext())
        recipeDao = db.recipeDao()
        favoriteDao = db.favoriteDao()
        repository = LocalRepository(recipeDao, favoriteDao)
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
        userEmailEditText = view.findViewById(R.id.userEmailEditText)
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
            // Call the method to add the recipe
            lifecycleScope.launch {
                addRecipe()
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
        val title = recipeTitleEditText.text.toString()
        val description = recipeDescriptionEditText.text.toString()

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(requireContext(), "Title and description are required!", Toast.LENGTH_SHORT).show()
            return
        }

        val thumbnailUrl = thumbnailUrlEditText.text.toString()
        val keywords = keywordsEditText.text.toString()

        var isPublic : Boolean = if (isPublicCheckbox.isChecked) true
        else false

        val userEmail = userEmailEditText.text.toString()
        val originalVideoUrl = originalVideoUrlEditText.text.toString()
        val country = countryEditText.text.toString()
        val numServings = numServingsEditText.text.toString().toIntOrNull() ?: 0
        val components = mutableListOf<ComponentModel>()

        // Iterate through dynamic fields (we assume that dynamic fields are contained in 'dynamicFieldsLayout')
        for (i in 0 until dynamicFieldsLayout.childCount) {
            val dynamicRow = dynamicFieldsLayout.getChildAt(i) as LinearLayout
            val ingredientEditText = dynamicRow.getChildAt(0) as EditText
            val quantityEditText = dynamicRow.getChildAt(1) as EditText
            val unitEditText = dynamicRow.getChildAt(2) as EditText

            val ingredientName = ingredientEditText.text.toString()
            val quantity = quantityEditText.text.toString()
            val unitName = unitEditText.text.toString()

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

        val instructions = mutableListOf<InstructionModel>()
        for (i in 0 until instructionsContainer.childCount) {
            val instructionEditText = instructionsContainer.getChildAt(i) as EditText
            val instructionText = instructionEditText.text.toString()
            if (instructionText.isNotEmpty()) {
                val instruction = InstructionModel(
                    instructionID = instructionCounter++,
                    displayText = instructionText,
                    position = instructions.size + 1
                )
                instructions.add(instruction)
            }
        }

        val calories = caloriesEditText.text.toString().toIntOrNull() ?: 0
        val fat = fatEditText.text.toString().toIntOrNull() ?: 0
        val protein = proteinEditText.text.toString().toIntOrNull() ?: 0
        val sugar = sugarEditText.text.toString().toIntOrNull() ?: 0
        val carbs = carbsEditText.text.toString().toIntOrNull() ?: 0
        val fiber = fiberEditText.text.toString().toIntOrNull() ?: 0

        val nutrition = NutritionModel(
            calories = calories,
            protein = protein,
            fat = fat,
            carbohydrates = carbs,
            sugar = sugar,
            fiber = fiber
        )

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
            nutrition = nutrition,
            isFavorite = false
        )

        // Insert into Room database
        repository.insertRecipe(newRecipe)

        Log.d("AddNewRecipeFragment", "New recipe added: $newRecipeId")
        Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_SHORT).show()
    }
}
