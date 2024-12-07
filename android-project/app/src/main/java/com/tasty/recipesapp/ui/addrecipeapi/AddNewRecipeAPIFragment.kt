package com.tasty.recipesapp.ui.addrecipeapi

import android.content.ContentValues.TAG
import android.content.Intent
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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.tasty.recipesapp.R
import com.tasty.recipesapp.models.recipe.RecipeModel
import com.tasty.recipesapp.models.recipe.recipemodels.ComponentModel
import com.tasty.recipesapp.models.recipe.recipemodels.IngredientModel
import com.tasty.recipesapp.models.recipe.recipemodels.InstructionModel
import com.tasty.recipesapp.models.recipe.recipemodels.MeasurementModel
import com.tasty.recipesapp.models.recipe.recipemodels.NutritionModel
import com.tasty.recipesapp.models.recipe.recipemodels.UnitModel
import com.tasty.recipesapp.restapi.auth.TokenProvider
import com.tasty.recipesapp.restapi.client.OAuthRetrofitClient
import com.tasty.recipesapp.restapi.client.RecipeAPIClient
import com.tasty.recipesapp.restapi.client.RetrofitClient
import com.tasty.recipesapp.restapi.service.RecipeService
import kotlinx.coroutines.launch

class AddNewRecipeAPIFragment : Fragment()
{
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
    private var instructionCounter = 1

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK && result.data != null) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResult(task)
            } else {
                Log.d("AddRecipe","Sign-in canceled or failed.")
                Toast.makeText(requireContext(), "Sign-in canceled or failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_new_recipe, container, false)

        configureGoogleSignIn()

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
            //checkLoginAndAddRecipe()
            TokenProvider(requireContext()).setAuthToken("eyJhbGciOiJSUzI1NiIsImtpZCI6IjJjOGEyMGFmN2ZjOThmOTdmNDRiMTQyYjRkNWQwODg0ZWIwOTM3YzQiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIxNjI1ODkxMzM3NDgtcWpndWZzNnJ2NDRmY3J0NHE4ZHN0cmU2djFlbG80Y3MuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiIxNjI1ODkxMzM3NDgtcWpndWZzNnJ2NDRmY3J0NHE4ZHN0cmU2djFlbG80Y3MuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDY1OTc4MTg4NzA1MTkwOTYwMzUiLCJoZCI6InN0dWRlbnQubXMuc2FwaWVudGlhLnJvIiwiZW1haWwiOiJlcnNlay5iZWF0cmljZUBzdHVkZW50Lm1zLnNhcGllbnRpYS5ybyIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhdF9oYXNoIjoiR05CN0FueW1yUzZLUGpWQUhoMWlWdyIsIm5hbWUiOiLDiXJzZWsgQmVhdHJpY2UtQWRyaWVubmUiLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EvQUNnOG9jSlBMei1rRDQxYmQzVy1xSVJIRTJPalVsWmV2X0N4cmZpcTg2UWdsbkxGbWJBWjVCV1Y9czk2LWMiLCJnaXZlbl9uYW1lIjoiw4lyc2VrIiwiZmFtaWx5X25hbWUiOiJCZWF0cmljZS1BZHJpZW5uZSIsImlhdCI6MTczMzU1Mzg0NywiZXhwIjoxNzMzNTU3NDQ3fQ.Zt2zT5fPrZP5Arb4HTs0tqefc6NZXsiu6I7bU19u2Wan2GLgFAldU8qiSgkT8i1u_p5yOwSy8RcnvztpsIAM1n-aVSJhy3edB8ACLyt104BhAWZeOb2BE__iYJA7ygxSLiXZITjKeIL1XcCKSYq5PLfAnZyqyvd7VhNZpyXjvr42lqtdIRpTUX0L4IWqhbYp3BVHsqXDDRS16MybY2s8GenDcYV_m7CQTbnsJVs_eLoDLu6BmvZrIt-DePKCTQrNP0msLI9pgtUUJr4vuUB74WiYUMDpsFdbRc5OTcy5-XwgUUA0K7vJf-U5i_NXsTOYjBJIC-7J8nbsTCj-JNd6RA")
            lifecycleScope.launch {
                val service = RetrofitClient(requireContext()).getRecipeService()
                addRecipe(service,"eyJhbGciOiJSUzI1NiIsImtpZCI6IjJjOGEyMGFmN2ZjOThmOTdmNDRiMTQyYjRkNWQwODg0ZWIwOTM3YzQiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIxNjI1ODkxMzM3NDgtcWpndWZzNnJ2NDRmY3J0NHE4ZHN0cmU2djFlbG80Y3MuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiIxNjI1ODkxMzM3NDgtcWpndWZzNnJ2NDRmY3J0NHE4ZHN0cmU2djFlbG80Y3MuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDY1OTc4MTg4NzA1MTkwOTYwMzUiLCJoZCI6InN0dWRlbnQubXMuc2FwaWVudGlhLnJvIiwiZW1haWwiOiJlcnNlay5iZWF0cmljZUBzdHVkZW50Lm1zLnNhcGllbnRpYS5ybyIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhdF9oYXNoIjoiR05CN0FueW1yUzZLUGpWQUhoMWlWdyIsIm5hbWUiOiLDiXJzZWsgQmVhdHJpY2UtQWRyaWVubmUiLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EvQUNnOG9jSlBMei1rRDQxYmQzVy1xSVJIRTJPalVsWmV2X0N4cmZpcTg2UWdsbkxGbWJBWjVCV1Y9czk2LWMiLCJnaXZlbl9uYW1lIjoiw4lyc2VrIiwiZmFtaWx5X25hbWUiOiJCZWF0cmljZS1BZHJpZW5uZSIsImlhdCI6MTczMzU1Mzg0NywiZXhwIjoxNzMzNTU3NDQ3fQ.Zt2zT5fPrZP5Arb4HTs0tqefc6NZXsiu6I7bU19u2Wan2GLgFAldU8qiSgkT8i1u_p5yOwSy8RcnvztpsIAM1n-aVSJhy3edB8ACLyt104BhAWZeOb2BE__iYJA7ygxSLiXZITjKeIL1XcCKSYq5PLfAnZyqyvd7VhNZpyXjvr42lqtdIRpTUX0L4IWqhbYp3BVHsqXDDRS16MybY2s8GenDcYV_m7CQTbnsJVs_eLoDLu6BmvZrIt-DePKCTQrNP0msLI9pgtUUJr4vuUB74WiYUMDpsFdbRc5OTcy5-XwgUUA0K7vJf-U5i_NXsTOYjBJIC-7J8nbsTCj-JNd6RA" )
            }
        }

        return view
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            val authCode = account?.serverAuthCode
            if (authCode != null) {
                exchangeAuthorizationCodeForTokens(authCode)
            } else {
                Log.d("AddRecipe","Failed to get authorization code.")
                Toast.makeText(requireContext(), "Failed to get authorization code.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ApiException) {
            Log.d("AddRecipe","Sign-in failed.")
            Toast.makeText(requireContext(), "Sign-in failed.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun exchangeAuthorizationCodeForTokens(authCode: String) {
        lifecycleScope.launch {
            try {
                val response = OAuthRetrofitClient.api.exchangeAuthorizationCode(
                    clientId = "162589133748-qjgufs6rv44fcrt4q8dstre6v1elo4cs.apps.googleusercontent.com",
                    clientSecret = "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",
                    authorizationCode = authCode
                )
                if (response.isSuccessful) {
                    val tokens = response.body()
                    tokens?.id_token?.let {
                        TokenProvider(requireContext()).setAuthToken(it)
                        Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Token exchange failed.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error during token exchange.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configureGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode("162589133748-qjgufs6rv44fcrt4q8dstre6v1elo4cs.apps.googleusercontent.com")
            .requestEmail()
            .requestProfile()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        Log.d("AddRecipe", "Launching Google Sign-In Intent: $signInIntent")
        googleSignInLauncher.launch(signInIntent)
    }

    private fun checkLoginAndAddRecipe() {
        val token = TokenProvider(requireContext()).getAuthToken()
        if (token.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please log in to add a recipe.", Toast.LENGTH_SHORT).show()
            signInWithGoogle()
        } else {
            lifecycleScope.launch {
                val service = RetrofitClient(requireContext()).getRecipeService()
                addRecipe(service, token)
            }
        }
    }

    private suspend fun addRecipe(service: RecipeService, token: String) {
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

        val response = service.addRecipe(recipe)
        if (response.isSuccessful) {
            Toast.makeText(requireContext(), "Recipe uploaded successfully!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Failed to upload recipe: ${response.message()}", Toast.LENGTH_SHORT).show()
        }
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
}