package com.tasty.recipesapp.ui.addrecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tasty.recipesapp.R
import com.tasty.recipesapp.models.RecipeModel
import com.tasty.recipesapp.repository.ProfileRepository
import com.tasty.recipesapp.repository.RecipeRepository

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
    private lateinit var addRecipeButton: Button
    private lateinit var profileRepository: ProfileRepository
    private lateinit var recipeRepository: RecipeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileRepository = ProfileRepository(requireContext())
        recipeRepository = RecipeRepository(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        addRecipeButton = view.findViewById(R.id.addRecipeButton)

        addRecipeButton.setOnClickListener {
            addRecipe()
        }

        return view
    }

    private fun addRecipe()
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
        val components = componentsEditText.text.toString().split(",").map { it.trim() }
        val instructions = instructionsEditText.text.toString().split(",").map { it.trim() }

        val existingRecipes = recipeRepository.getRecipes().toMutableList()
        val newRecipeId = if (existingRecipes.isNotEmpty())
        {
            existingRecipes.maxOf { it.recipeID } + 1
        }
        else 1

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
            instructions = instructions
        )

        val updatedRecipes = existingRecipes.toMutableList()
        updatedRecipes.add(newRecipe)
        recipeRepository.saveRecipes(updatedRecipes)

        Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_SHORT).show()
    }


}
