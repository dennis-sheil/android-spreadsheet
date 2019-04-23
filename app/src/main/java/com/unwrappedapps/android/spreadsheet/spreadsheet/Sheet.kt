package com.unwrappedapps.android.spreadsheet.spreadsheet


open class Sheet {

    var rowList : MutableList<Row> = mutableListOf()

    fun getNumberOfColumns() : Int {
        if (rowList.size == 0) return 0
        else return rowList[0].cellList.size
    }

    fun getRow(i : Int) : Row {
        if (i >= rowList.size) {
            rowList.add(Row()) // maybe do sanity check that i is not too large
        }
        return rowList[i]
    }

}