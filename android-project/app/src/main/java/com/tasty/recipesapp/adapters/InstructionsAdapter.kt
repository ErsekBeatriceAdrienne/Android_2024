package com.tasty.recipesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tasty.recipesapp.databinding.ItemInstructionBinding
import com.tasty.recipesapp.models.recipe.recipemodels.InstructionModel

class InstructionsAdapter(private val instructions: List<InstructionModel>) :
    RecyclerView.Adapter<InstructionsAdapter.InstructionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionViewHolder {
        val binding = ItemInstructionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InstructionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InstructionViewHolder, position: Int) {
        holder.bind(instructions[position])
    }

    override fun getItemCount(): Int = instructions.size

    inner class InstructionViewHolder(private val binding: ItemInstructionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(instruction: InstructionModel) {
            binding.instruction = instruction
            binding.executePendingBindings()
        }
    }
}