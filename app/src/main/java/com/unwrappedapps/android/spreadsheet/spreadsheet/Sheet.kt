package com.unwrappedapps.android.spreadsheet.spreadsheet


open class Sheet {

    val TOO_LARGE = 99999

    var rowList : MutableList<Row> = mutableListOf()

    var name : String = ""

    fun getNumberOfColumns() : Int {
        if (rowList.size == 0) return 0
        else return rowList[0].cellList.size
    }

    fun getRow(i : Int) : Row {

        // XXX: TOO_LARGE is a big number to catch bad input
        while (i >= rowList.size && i < TOO_LARGE) {
            rowList.add(Row())
        }
        return rowList[i]
    }

}