package com.unwrappedapps.android.spreadsheet.spreadsheet


open class Row {

    var cellList : MutableList<Cell> = mutableListOf()

    fun getCell(column : Int) : Cell {
        while(cellList.size <= column) {
            cellList.add(Cell())
        }
        return cellList[column]
    }

}