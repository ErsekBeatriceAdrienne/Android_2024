package com.tasty.recipesapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.tasty.recipesapp.R

class TodoAdapter(
    private var todos: MutableList<String>,
    private val onTodoUpdate: (String, Int) -> Unit,
    private val onTodoComplete: (Int) -> Unit
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val editText: EditText = itemView.findViewById(R.id.editTextTodo)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxTodo)

        fun bind(todo: String, position: Int) {
            editText.setText(todo)
            checkBox.isChecked = false

            // Listen for focus changes on the EditText
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val updatedText = editText.text.toString()
                    if (updatedText.isBlank()) {
                        // Ha üres a todo, akkor nem frissítjük
                        return@setOnFocusChangeListener
                    }
                    onTodoUpdate(updatedText, position)  // Update todo when focus is lost
                }
            }

            // Listen for checkbox status changes
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    onTodoComplete(position)  // Complete the todo if checked
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todos[position], position)
    }

    override fun getItemCount(): Int = todos.size

    // Update todos after changes
    fun updateTodos(newTodos: List<String>) {
        todos = newTodos.toMutableList()
        notifyDataSetChanged()
    }
}
