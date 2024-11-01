package com.tasty.recipesapp.ui.recipe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tasty.recipesapp.R
import com.tasty.recipesapp.repository.RecipeRepository
import com.tasty.recipesapp.viewmodels.RecipeListViewModel
import com.tasty.recipesapp.viewmodels.RecipeViewModelFactory

class RecipeFragment : Fragment() {

    private lateinit var viewModel: RecipeListViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InstructionAdapter // Create this adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view_instructions)

        // Set the LayoutManager programmatically
        recyclerView.layoutManager = LinearLayoutManager(context)

        val repository = RecipeRepository()
        val factory = RecipeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(RecipeListViewModel::class.java)

        // Observe the instructions LiveData
        viewModel.instructions.observe(viewLifecycleOwner, Observer { instructions ->
            // Update the adapter with the instruction list
            adapter = InstructionAdapter(instructions) // Create this adapter class
            recyclerView.adapter = adapter

            // Log the instruction data
            for (instruction in instructions) {
                Log.d("InstructionData", "Instruction ID: ${instruction.id}")
                Log.d("InstructionData", "Display Text: ${instruction.displayText}")
            }
        })

        // Fetch the instructions
        viewModel.fetchInstructions()
    }
}
