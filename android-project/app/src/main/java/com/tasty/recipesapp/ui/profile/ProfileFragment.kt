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

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.buttonRecipeOfTheDay.setOnClickListener {
            get_recipe_of_the_day();
        }

        // Set up Google Sign-In client
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), viewModel.getGoogleSignInOptions())

        binding.googleSignInButton.setOnClickListener {
            signInWithGoogle()
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

    // Google Sign-In method
    private fun signInWithGoogle() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // Handle the result of the sign-in
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign-In was successful, authenticate with Firebase or handle the account
                val account = task.getResult(ApiException::class.java)
                Log.d("ProfileFragment", "Signed in successfully: ${account?.displayName}")
                // Do something with the signed-in account (e.g., save to SharedPreferences or navigate)
            } catch (e: ApiException) {
                Log.w("ProfileFragment", "Google sign-in failed", e)
            }
        }
    }

}
