package com.unwrappedapps.android.spreadsheet.spreadsheet

import com.unwrappedapps.android.spreadsheet.spreadsheet.csv.CsvWorkbook
import com.unwrappedapps.android.spreadsheet.spreadsheet.poi.PoiWorkbook
import java.io.InputStream


class Spreadsheet {

    var workbook : Workbook = Workbook()

    // empty spreadsheet
    constructor() {
        workbook = Workbook()
    }

    // spreadsheetFormat from string to other
    constructor(inputStream: InputStream, spreadsheetFormat: String?) {

        if (spreadsheetFormat.equals("csv")) {
            workbook = CsvWorkbook(inputStream)
        }
        else if (spreadsheetFormat.equals("xls")) {
            workbook = PoiWorkbook(inputStream)
        }
        else if (spreadsheetFormat.equals("xlsx")) {
            workbook = PoiWorkbook(inputStream)
        }

    }


}