package com.tasty.recipesapp.repository

import android.content.Context
import androidx.core.content.edit

class TodoRepository(private val context: Context) {

    fun loadTodos(): List<String> {
        val sharedPreferences = context.getSharedPreferences("TodoPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getStringSet("todos", emptySet())?.toList() ?: emptyList()
    }

    fun addTodo(todo: String) {
        val sharedPreferences = context.getSharedPreferences("TodoPrefs", Context.MODE_PRIVATE)
        val todos = loadTodos().toMutableList()
        todos.add(todo)
        sharedPreferences.edit {
            putStringSet("todos", todos.toSet())
        }
    }

    fun updateTodo(updatedText: String, position: Int) {
        val sharedPreferences = context.getSharedPreferences("TodoPrefs", Context.MODE_PRIVATE)
        val todos = loadTodos().toMutableList()
        todos[position] = updatedText
        sharedPreferences.edit {
            putStringSet("todos", todos.toSet())
        }
    }

    fun completeTodo(position: Int) {
        val sharedPreferences = context.getSharedPreferences("TodoPrefs", Context.MODE_PRIVATE)
        val todos = loadTodos().toMutableList()
        todos.removeAt(position)
        sharedPreferences.edit {
            putStringSet("todos", todos.toSet())
        }
    }
}
