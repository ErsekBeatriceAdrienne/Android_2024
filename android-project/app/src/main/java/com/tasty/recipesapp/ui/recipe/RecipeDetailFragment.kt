package com.tasty.recipesapp.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.tasty.recipesapp.databinding.FragmentRecipeDetailBinding
import com.tasty.recipesapp.models.recipe.RecipeModel
import com.tasty.recipesapp.repository.restapi.RecipeAPIRepository
import kotlinx.coroutines.launch

class RecipeDetailFragment : Fragment()
{
    private lateinit var binding: FragmentRecipeDetailBinding
    private val apiRepository: RecipeAPIRepository = RecipeAPIRepository()
    private lateinit var recipe: RecipeModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            arguments?.getString("recipeId")?.let { recipeId ->
                val fetchedRecipe = fetchRecipeById(recipeId.toLong())
                if (fetchedRecipe != null) {
                    recipe = fetchedRecipe
                    displayRecipeDetails(recipe)
                } else {
                    Toast.makeText(requireContext(), "Recipe not found", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeId = arguments?.getString("recipeId")
        if (recipeId == null) {
            Toast.makeText(requireContext(), "Recipe ID is missing", Toast.LENGTH_SHORT).show()
            return
        }

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private suspend fun fetchRecipeById(recipeId: Long): RecipeModel? {
        val recipes = apiRepository.getRecipesFromApi()
        return recipes.find { it.recipeID.toLong() == recipeId }
    }

    private fun displayRecipeDetails(recipe: RecipeModel) {
        binding.recipeTitle.text = recipe.name
        binding.recipeDescription.text = recipe.description

        // Display the thumbnail image
        if (recipe.thumbnailUrl.isNotEmpty()) {
            Glide.with(requireContext())
                .load(recipe.thumbnailUrl)
                .into(binding.recipeThumbnail)
            binding.recipeThumbnail.visibility = View.VISIBLE
        }

        // Display Servings
        binding.recipeNumServings.text = "Servings: ${recipe.numServings}"

        // Display Ingredients
        binding.ingredientsContainer.removeAllViews()
        recipe.components.forEach { component ->
            val ingredientText =
                "${component.position}. ${component.ingredient.name} - ${component.measurement.quantity} ${component.measurement.unit.displaySingular}"
            val textView = TextView(context).apply {
                text = ingredientText
                setPadding(0, 4, 0, 4)
            }
            binding.ingredientsContainer.addView(textView)
        }

        // Display Instructions
        binding.instructionsContainer.removeAllViews()
        recipe.instructions.forEach { instruction ->
            val instructionText = "${instruction.position}. ${instruction.displayText}"
            val textView = TextView(context).apply {
                text = instructionText
                setPadding(0, 4, 0, 4)
            }
            binding.instructionsContainer.addView(textView)
        }

        // Display Nutrition
        val nutritionText = """
        Calories: ${recipe.nutrition.calories} kcal
        Protein: ${recipe.nutrition.protein} g
        Fat: ${recipe.nutrition.fat} g
        Carbs: ${recipe.nutrition.carbohydrates} g
        Sugar: ${recipe.nutrition.sugar} g
        Fiber: ${recipe.nutrition.fiber} g
    """.trimIndent()

        binding.nutritionContainer.removeAllViews()
        val nutritionTextView = TextView(context).apply {
            text = nutritionText
            setPadding(0, 4, 0, 4)
        }
        binding.nutritionContainer.addView(nutritionTextView)

        // Display Video URL (if present)
        if (!recipe.originalVideoUrl.isNullOrEmpty()) {
            setupWebView(binding.recipeVideoPlayer, recipe.originalVideoUrl)
            binding.recipeVideoPlayer.visibility = View.VISIBLE
        } else {
            binding.recipeVideoPlayer.visibility = View.GONE
        }

    }

    private fun setupWebView(webView: WebView, url: String) {
        webView.settings.apply {
            javaScriptEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            domStorageEnabled = true
            mediaPlaybackRequiresUserGesture = false
            allowFileAccess = true
            allowContentAccess = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()

        // YouTube beágyazott URL kezelése
        val embedUrl = if (url.contains("youtube.com") || url.contains("youtu.be")) {
            convertToEmbedUrl(url)
        } else {
            url
        }

        webView.loadUrl(embedUrl)
    }

    // Konvertáljuk a YouTube URL-t beágyazott formátumra
    private fun convertToEmbedUrl(url: String): String {
        return when {
            url.contains("youtu.be") -> {
                val videoId = url.substringAfterLast("/")
                "https://www.youtube.com/embed/$videoId"
            }
            url.contains("watch?v=") -> {
                val videoId = url.substringAfter("watch?v=").substringBefore("&")
                "https://www.youtube.com/embed/$videoId"
            }
            else -> url
        }
    }
}