package com.unwrappedapps.android.spreadsheet.spreadsheet.poi


class PoiRow() : com.unwrappedapps.android.spreadsheet.spreadsheet.Row() {

    lateinit var pRow : org.apache.poi.ss.usermodel.Row

    constructor(row: org.apache.poi.ss.usermodel.Row) : this() {
        pRow = row

        val numberOfCells = pRow.physicalNumberOfCells

        for (i in 0..numberOfCells-1) {
            val pCell = pRow.getCell(i)
            // TODO: decide if should be skipped or a blank one

            if (pCell != null) {
                val cell = PoiCell(pCell)
                cellList.add(cell)
            }
        }

    }

}
