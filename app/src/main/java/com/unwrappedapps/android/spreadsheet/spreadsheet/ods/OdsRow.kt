package com.unwrappedapps.android.spreadsheet.spreadsheet.ods

import com.unwrappedapps.android.spreadsheet.spreadsheet.Row


class OdsRow : Row() {
    init {
        cellList = mutableListOf()
    }

    fun add(odsCell : OdsCell) {
        cellList.add(odsCell)
    }
}
