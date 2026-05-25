package com.example.mob_systeme2

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectCategoriesBottomSheet : BottomSheetDialogFragment() {
    companion object {
        // Bei Fragmenten keine eigenen Konstruktorparameter Bundle/newInstance verwenden.
        // Android kann das Fragment später selbst neu erzeugen (ohne Parameter) -> Konstruktor leer -> fehler
        fun newInstance(selectedIds: Set<String>): SelectCategoriesBottomSheet{
            val sheet = SelectCategoriesBottomSheet()
            return sheet
        }
    }
}