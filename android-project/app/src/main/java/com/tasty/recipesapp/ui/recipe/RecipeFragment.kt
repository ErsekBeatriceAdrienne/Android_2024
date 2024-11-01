package com.tasty.recipesapp.ui.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tasty.recipesapp.adapters.InstructionsAdapter
import com.tasty.recipesapp.databinding.FragmentRecipeBinding
import com.tasty.recipesapp.models.RecipeViewModel
import com.tasty.recipesapp.models.RecipeViewModelFactory
import com.tasty.recipesapp.repository.RecipeRepository

class RecipeFragment : Fragment() {
    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var binding: FragmentRecipeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipeBinding.inflate(inflater, container, false)
        val recipeRepository = RecipeRepository(requireContext())
        val viewModelFactory = RecipeViewModelFactory(recipeRepository)
        recipeViewModel = ViewModelProvider(this, viewModelFactory).get(RecipeViewModel::class.java)

        // Bind the ViewModel
        binding.viewModel = recipeViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Set up the RecyclerView layout manager and adapter
        binding.recyclerViewInstructions.layoutManager = LinearLayoutManager(requireContext())
        recipeViewModel.instructions.observe(viewLifecycleOwner) { instructions ->
            val adapter = InstructionsAdapter(instructions)
            binding.recyclerViewInstructions.adapter = adapter
        }

        return binding.root
    }
}
