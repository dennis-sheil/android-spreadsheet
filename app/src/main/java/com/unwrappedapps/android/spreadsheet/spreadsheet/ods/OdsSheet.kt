package com.unwrappedapps.android.spreadsheet.spreadsheet.ods

import com.unwrappedapps.android.spreadsheet.spreadsheet.Sheet


// XXX: We can probably rename this to OdsTable if we want to
class OdsSheet : Sheet() {

    init {
        rowList = mutableListOf()
    }

    fun add(odsRow: OdsRow) {
        rowList.add(odsRow)
    }

}