package com.unwrappedapps.android.spreadsheet.spreadsheet


open class Workbook {

    var sheetList: MutableList<Sheet> = mutableListOf()

    var currentSheet : Int

    init {
        val sheet = Sheet()
        sheetList.add(sheet)
        currentSheet = 0
    }


}