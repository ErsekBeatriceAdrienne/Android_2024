package com.tasty.recipesapp.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tasty.recipesapp.R
import com.tasty.recipesapp.adapters.TodoAdapter
import com.tasty.recipesapp.models.home.HomeViewModel
import com.tasty.recipesapp.models.home.HomeViewModelFactory

class HomeFragment : Fragment() {
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // ViewModel inicializálása
        homeViewModel = ViewModelProvider(
            this,
            HomeViewModelFactory(requireActivity().application)
        ).get(HomeViewModel::class.java)

        // RecyclerView beállítása
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewTodo)
        todoAdapter = TodoAdapter(
            todos = homeViewModel.todos.value?.toMutableList() ?: mutableListOf(),
            onTodoUpdate = { updatedText, position ->
                homeViewModel.updateTodo(updatedText, position)
            },
            onTodoComplete = { position ->
                homeViewModel.completeTodo(position)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = todoAdapter

        // Add Plan gomb kezelés
        val addButton = view.findViewById<Button>(R.id.buttonAddPlan)
        addButton.setOnClickListener {
            val todos = homeViewModel.todos.value ?: emptyList()

            // Ellenőrizzük, hogy van-e üres plan
            if (todos.isNotEmpty() && todos.last().isBlank()) {
                Toast.makeText(requireContext(), "Van már üres plan.", Toast.LENGTH_SHORT).show()
            } else {
                // Ha nincs üres plan, akkor új üres plan hozzáadása
                showAddTodoDialog()
            }
        }

        // LiveData figyelése
        homeViewModel.todos.observe(viewLifecycleOwner) { todos ->
            todoAdapter.updateTodos(todos)
        }

        return view
    }

    // Dialog a plan szövegének megadásához
    private fun showAddTodoDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Új Plan")

        // Létrehozzuk az EditTextet, ahol a felhasználó beírhatja a szöveget
        val input = EditText(requireContext())
        input.hint = "Írd be a plan szövegét"
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            val todoText = input.text.toString()
            if (todoText.isNotEmpty()) {
                homeViewModel.addTodo(todoText)  // Add a new todo with the entered text
            } else {
                Toast.makeText(requireContext(), "Kérlek, adj meg egy szöveget!", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel", null)

        builder.show()
    }
}

