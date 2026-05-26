package com.example.mob_systeme2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_systeme2.model.TodoCategory

class CategorySelectionAdapter(
    private val categories: List<TodoCategory>,
    private val selectedIds: Set<String>,
    private val onCheckedChanged: (String, Boolean) -> Unit
) : RecyclerView.Adapter<CategorySelectionAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_multiple_choice, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        val checked = selectedIds.contains(category.id)

        holder.checkedText.text = category.name
        holder.checkedText.isChecked = checked
        holder.checkedText.setOnClickListener {
            val nowChecked = !holder.checkedText.isChecked
            holder.checkedText.isChecked = nowChecked
            onCheckedChanged(category.id, nowChecked)
        }
    }

    override fun getItemCount(): Int = categories.size

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkedText: CheckedTextView = view.findViewById(android.R.id.text1)
    }
}
