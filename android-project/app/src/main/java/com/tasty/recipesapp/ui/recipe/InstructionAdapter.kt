package com.tasty.recipesapp.ui.recipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tasty.recipesapp.R
import com.tasty.recipesapp.models.InstructionModel

class InstructionAdapter(private val instructions: List<InstructionModel>) :
    RecyclerView.Adapter<InstructionAdapter.InstructionViewHolder>() {

    class InstructionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val instructionTextView: TextView = view.findViewById(R.id.instruction_text) // Adjust this ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_instruction, parent, false) // Create this layout for a single instruction
        return InstructionViewHolder(view)
    }

    override fun onBindViewHolder(holder: InstructionViewHolder, position: Int) {
        val instruction = instructions[position]
        holder.instructionTextView.text = instruction.displayText
    }

    override fun getItemCount() = instructions.size
}
