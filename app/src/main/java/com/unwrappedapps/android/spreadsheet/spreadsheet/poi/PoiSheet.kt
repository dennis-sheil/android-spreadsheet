package com.unwrappedapps.android.spreadsheet.spreadsheet.poi

import com.unwrappedapps.android.spreadsheet.spreadsheet.Sheet as MySheet

class PoiSheet() : com.unwrappedapps.android.spreadsheet.spreadsheet.Sheet() {

    lateinit var pSheet : org.apache.poi.ss.usermodel.Sheet

    constructor(sheet: org.apache.poi.ss.usermodel.Sheet) : this() {
        pSheet = sheet

        name = pSheet.sheetName

        val numberOfRows = sheet.physicalNumberOfRows

        for (i in 0..numberOfRows-1) {
            val pRow = pSheet.getRow(i)

            // TODO: decide if should be skipped or a blank one
            if (pRow != null) {
                val row = PoiRow(pRow)
                rowList.add(row)
            }
        }
    }

}
