package com.example.mob_systeme2

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectCategoriesBottomSheet : BottomSheetDialogFragment() {

    interface Callback {
        fun onCategoriesPicked(ids: Set<String>)
    }

    private var callback: Callback? = null
    private val currentSelectedIds = mutableSetOf<String>()

    override fun onAttach(context: Context){
        super.onAttach(context)
        callback = context as? Callback
            ?: error("Error")
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
    ): View{
        return inflater.inflate(R.layout.bottom_sheet_edit_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)



        view.findViewById<Button>(R.id.btnApplySelectedCategories).setOnClickListener {
            callback?.onCategoriesPicked(currentSelectedIds.toSet())
            dismiss()
        }
    }

    override fun onDetach(){
        super.onDetach()
        callback = null
    }

    companion object {
        private const val ARG_SELECTED_IDS = "arg_selected_ids"

        fun newInstance(selectedIds: Set<String>): SelectCategoriesBottomSheet{
            return SelectCategoriesBottomSheet().apply {
                arguments = Bundle().apply{
                    putStringArrayList(ARG_SELECTED_IDS, ArrayList(selectedIds))
                }
            }
        }
    }
}