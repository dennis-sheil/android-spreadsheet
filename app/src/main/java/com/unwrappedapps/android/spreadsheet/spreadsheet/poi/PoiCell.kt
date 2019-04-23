package com.unwrappedapps.android.spreadsheet.spreadsheet.poi

import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DateUtil

class PoiCell() : com.unwrappedapps.android.spreadsheet.spreadsheet.Cell() {

    lateinit var pCell: org.apache.poi.ss.usermodel.Cell

    constructor(cell: org.apache.poi.ss.usermodel.Cell) : this() {

        pCell = cell
        val value : String?

        when (cell.cellTypeEnum) {

            CellType.FORMULA -> value = cell.cellFormula

            CellType.NUMERIC -> if (DateUtil.isCellDateFormatted(cell))
                value = "" + cell.dateCellValue
            else
                value = "" + cell.numericCellValue
            //CellType.STRING
            else -> value = cell.stringCellValue
        }

        if (value != null) {
            cellValue = value
        }

    }
}
