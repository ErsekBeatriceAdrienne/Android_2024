package com.tasty.recipesapp.models.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.tasty.recipesapp.repository.TodoRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val todoRepository = TodoRepository(application)
    val todos = MutableLiveData<List<String>>()

    init {
        loadTodos()
    }

    fun loadTodos() {
        viewModelScope.launch {
            todos.postValue(todoRepository.loadTodos())
        }
    }

    // Új todo hozzáadása
    fun addTodo(todoText: String) {
        val todosList = todos.value?.toMutableList() ?: mutableListOf()
        todosList.add(todoText)  // Add the new todo text
        todos.value = todosList.toList()

        // Persist to repository
        todoRepository.addTodo(todoText)
    }

    // Todo frissítése
    fun updateTodo(updatedText: String, position: Int) {
        val todosList = todos.value?.toMutableList() ?: return
        if (position in todosList.indices) {
            todosList[position] = updatedText
            todos.value = todosList.toList()
        }
        todoRepository.updateTodo(updatedText, position)
    }

    // Todo kijelölésének kezelése (complete)
    fun completeTodo(position: Int) {
        val todosList = todos.value?.toMutableList() ?: return
        if (position in todosList.indices) {
            todosList.removeAt(position)
            todos.value = todosList
        }
        todoRepository.completeTodo(position)
    }
}
