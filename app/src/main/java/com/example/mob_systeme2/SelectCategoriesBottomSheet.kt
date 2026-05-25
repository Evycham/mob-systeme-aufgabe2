package com.example.mob_systeme2

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckedTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_systeme2.data.CategoryRepo
import com.example.mob_systeme2.model.TodoCategory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectCategoriesBottomSheet : BottomSheetDialogFragment() {

    interface Callback {
        fun onCategoriesPicked(ids: Set<String>)
    }

    private var callback: Callback? = null
    private val currentSelectedIds = mutableSetOf<String>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as? Callback
            ?: error("Host must implement SelectCategoriesBottomSheet.Callback")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val initialIds = arguments?.getStringArrayList(ARG_SELECTED_IDS).orEmpty()
        currentSelectedIds.clear()
        currentSelectedIds.addAll(initialIds)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bottom_sheet_select_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryAdapter = CategorySelectionAdapter(
            categories = CategoryRepo.categoryList,
            selectedIds = currentSelectedIds,
            onCheckedChanged = { categoryId, isChecked ->
                if (isChecked) currentSelectedIds.add(categoryId) else currentSelectedIds.remove(categoryId)
            }
        )
        val list = view.findViewById<RecyclerView>(R.id.rvSelectableCategories)
        list.layoutManager = LinearLayoutManager(requireContext())
        list.adapter = categoryAdapter

        view.findViewById<Button>(R.id.btnApplySelectedCategories).setOnClickListener {
            callback?.onCategoriesPicked(currentSelectedIds.toSet())
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    companion object {
        private const val ARG_SELECTED_IDS = "arg_selected_ids"

        fun newInstance(selectedIds: Set<String>): SelectCategoriesBottomSheet {
            return SelectCategoriesBottomSheet().apply {
                arguments = Bundle().apply {
                    putStringArrayList(ARG_SELECTED_IDS, ArrayList(selectedIds))
                }
            }
        }
    }
}

private class CategorySelectionAdapter(
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
