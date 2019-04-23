package com.unwrappedapps.android.spreadsheet.spreadsheet.csv

import com.unwrappedapps.android.spreadsheet.spreadsheet.Workbook
import java.io.InputStream

open class CsvWorkbook (inputStream: InputStream): Workbook() {


    init {
        var csvSheet : CsvSheet

        csvSheet = CsvSheet(inputStream)

        if (sheetList.size > 1) {
            sheetList.clear()
        }
        sheetList.set(0, csvSheet)
    }

}
