package com.example.mob_systeme2.sheets

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.mob_systeme2.R
import com.example.mob_systeme2.data.CategoryRepo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Bottom sheet for creating a new category or editing/deleting an existing one.
 */
class EditCategoryBottomSheet : BottomSheetDialogFragment() {

    /**
     * Notifies host activity that category data changed and UI should refresh.
     */
    interface Callback {
        fun onCategoryChanged()
    }

    private var callback: Callback? = null
    private var categoryId: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as? Callback
            ?: error("Host activity must implement EditCategoryBottomSheet.Callback")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = arguments?.getString(ARG_CATEGORY_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.bottom_sheet_edit_category, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameInput: EditText = view.findViewById(R.id.etCategoryNameInput)
        val colorSpinner: Spinner = view.findViewById(R.id.spColorList)
        val iconSpinner: Spinner = view.findViewById(R.id.spIconList)
        val saveButton: Button = view.findViewById(R.id.btnSaveCategory)
        val deleteButton: Button = view.findViewById(R.id.btnDeleteCategory)

        val colorAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, COLOR_KEYS)
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        colorSpinner.adapter = colorAdapter

        val iconAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ICON_KEYS)
        iconAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        iconSpinner.adapter = iconAdapter

        val existing = categoryId?.let { CategoryRepo.findCategory(it) }
        if (existing != null) {
            nameInput.setText(existing.name)
            colorSpinner.setSelection(COLOR_KEYS.indexOf(existing.colorKey).coerceAtLeast(0))
            iconSpinner.setSelection(ICON_KEYS.indexOf(existing.iconKey).coerceAtLeast(0))
        } else {
            deleteButton.visibility = View.GONE
        }

        saveButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val colorKey = colorSpinner.selectedItem.toString()
            val iconKey = iconSpinner.selectedItem.toString()

            val result = if (existing == null) {
                CategoryRepo.createCategory(name, colorKey, iconKey)
            } else {
                CategoryRepo.editCategory(existing.id, name, colorKey, iconKey)
            }
            if (result != null) {
                Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            callback?.onCategoryChanged()
            dismiss()
        }

        deleteButton.setOnClickListener {
            val id = existing?.id ?: return@setOnClickListener
            val result = CategoryRepo.deleteCategory(id)
            if (result != null) {
                Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            callback?.onCategoryChanged()
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    companion object {
        private const val ARG_CATEGORY_ID = "arg_category_id"
        private val COLOR_KEYS = listOf("blue", "green", "orange", "red", "gray")
        private val ICON_KEYS = listOf("book", "work", "home", "sport", "star")

        /**
         * Creates a sheet for a specific category id or for creation if id is null.
         */
        fun newInstance(categoryId: String?): EditCategoryBottomSheet {
            return EditCategoryBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATEGORY_ID, categoryId)
                }
            }
        }
    }
}
