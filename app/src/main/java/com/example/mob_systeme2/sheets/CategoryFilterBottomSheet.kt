package com.example.mob_systeme2.sheets

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_systeme2.R
import com.example.mob_systeme2.data.CategoryRepo
import com.example.mob_systeme2.model.TodoCategory
import com.example.mob_systeme2.ui.CategoryVisuals
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Bottom sheet for filtering todos by category and opening category editing.
 */
class CategoryFilterBottomSheet : BottomSheetDialogFragment() {

    /**
     * Callback methods consumed by the host activity.
     */
    interface Callback {
        fun onApplyCategoryFilter(selectedIds: Set<String>)
        fun onOpenCategoryEditor(categoryId: String?)
    }

    private var callback: Callback? = null
    private val selectedIds = mutableSetOf<String>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as? Callback
            ?: error("Host activity must implement CategoryFilterBottomSheet.Callback")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val initialIds = arguments?.getStringArrayList(ARG_SELECTED_IDS).orEmpty()
        selectedIds.clear()
        selectedIds.addAll(initialIds)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.bottom_sheet_category_filters, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CategoryFilterAdapter(
            categories = CategoryRepo.getCategories(),
            selectedIds = selectedIds,
            onChecked = { categoryId, checked ->
                if (checked) selectedIds.add(categoryId) else selectedIds.remove(categoryId)
            },
            onEdit = { categoryId ->
                dismiss()
                callback?.onOpenCategoryEditor(categoryId)
            }
        )

        view.findViewById<RecyclerView>(R.id.rvCategoryFilters).apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        view.findViewById<Button>(R.id.btnAddCategory).setOnClickListener {
            dismiss()
            callback?.onOpenCategoryEditor(null)
        }
        view.findViewById<Button>(R.id.btnApplyFilters).setOnClickListener {
            callback?.onApplyCategoryFilter(selectedIds.toSet())
            dismiss()
        }
        view.findViewById<Button>(R.id.btnClearFilters).setOnClickListener {
            selectedIds.clear()
            callback?.onApplyCategoryFilter(emptySet())
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    companion object {
        private const val ARG_SELECTED_IDS = "arg_selected_ids"

        /**
         * Creates a new filter sheet with preselected category ids.
         */
        fun newInstance(selectedIds: Set<String>): CategoryFilterBottomSheet {
            return CategoryFilterBottomSheet().apply {
                arguments = Bundle().apply {
                    putStringArrayList(ARG_SELECTED_IDS, ArrayList(selectedIds))
                }
            }
        }
    }
}

private class CategoryFilterAdapter(
    private val categories: List<TodoCategory>,
    private val selectedIds: Set<String>,
    private val onChecked: (String, Boolean) -> Unit,
    private val onEdit: (String) -> Unit
) : RecyclerView.Adapter<CategoryFilterAdapter.FilterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_filter, parent, false)
        return FilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val category = categories[position]
        holder.checkBox.text = "${CategoryVisuals.iconFor(category.iconKey)}  ${category.name}"
        holder.checkBox.isChecked = selectedIds.contains(category.id)
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onChecked(category.id, isChecked)
        }
        holder.editButton.setOnClickListener { onEdit(category.id) }
    }

    override fun getItemCount(): Int = categories.size

    /**
     * View holder for one filter row (checkbox + edit button).
     */
    class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.cbCategoryFilter)
        val editButton: Button = itemView.findViewById(R.id.btnEditCategory)
    }
}
