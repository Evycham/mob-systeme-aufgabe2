package com.example.mob_systeme2.sheets

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_systeme2.R
import com.example.mob_systeme2.adapters.CategorySelectionAdapter
import com.example.mob_systeme2.data.CategoryRepo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Bottom sheet for selecting multiple categories of a todo.
 */
class SelectCategoriesBottomSheet : BottomSheetDialogFragment() {

    /**
     * Callback for returning selected category ids to the host activity.
     */
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
            categories = CategoryRepo.getCategories(),
            selectedIds = currentSelectedIds,
            onCheckedChanged = { categoryId, isChecked ->
                if (isChecked) currentSelectedIds.add(categoryId) else currentSelectedIds.remove(
                    categoryId
                )
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

        /**
         * Creates a new sheet and provides preselected category ids.
         */
        fun newInstance(selectedIds: Set<String>): SelectCategoriesBottomSheet {
            return SelectCategoriesBottomSheet().apply {
                arguments = Bundle().apply {
                    putStringArrayList(ARG_SELECTED_IDS, ArrayList(selectedIds))
                }
            }
        }
    }
}
