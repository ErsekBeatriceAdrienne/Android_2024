package com.tasty.recipesapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.tasty.recipesapp.adapters.ProfileRecipeAdapter
import com.tasty.recipesapp.database.RecipeDatabase
import com.tasty.recipesapp.databinding.FragmentProfileBinding
import com.tasty.recipesapp.models.profile.ProfileRecipesViewModel
import com.tasty.recipesapp.models.profile.ProfileViewModelFactory
import com.tasty.recipesapp.repository.roomdatabase.LocalDBRepository

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var profileAdapter: ProfileRecipeAdapter

    private val database by lazy { RecipeDatabase.getDatabase(requireContext()) }
    private val viewModel: ProfileRecipesViewModel by viewModels {
        ProfileViewModelFactory(LocalDBRepository(database.recipeDao(), database.favoriteDao()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.buttonRecipeOfTheDay.setOnClickListener {
            get_recipe_of_the_day();
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favoriteRecipesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        profileAdapter = ProfileRecipeAdapter(emptyList()) { recipe ->
            val action = ProfileFragmentDirections.actionProfileFragmentToRecipeDetailFragment(recipe.recipeID.toString())
            findNavController().navigate(action)
        }
        binding.favoriteRecipesRecyclerView.adapter = profileAdapter

        viewModel.recipes.observe(viewLifecycleOwner, Observer { recipes ->
            profileAdapter.updateRecipes(recipes)
        })
    }


    private fun get_recipe_of_the_day()
    {
        viewModel.getRandomRecipe()

        binding.cardViewRandomRecipe.visibility = View.VISIBLE
        binding.textViewRandomRecipe.visibility = View.VISIBLE
        binding.textViewRandomRecipeDescription.visibility = View.VISIBLE

        viewModel.randomRecipe.observe(viewLifecycleOwner) { recipe ->
            binding.textViewRandomRecipe.text = recipe?.name ?: "No recipe found!"
            binding.textViewRandomRecipeDescription.text = " - ${recipe?.description}" ?: ""
        }
    }
}
