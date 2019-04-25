package com.unwrappedapps.android.spreadsheet

import android.os.AsyncTask
import com.unwrappedapps.android.spreadsheet.spreadsheet.Spreadsheet
import java.lang.ref.WeakReference


class SearchTask
internal constructor(context: SheetActivity) : AsyncTask<SheetActivity.SearchData, Void, Unit>() {

    private val weakContext : WeakReference<SheetActivity> = WeakReference(context)

    var searchRow = 0
    var searchColumn = 0
    var searchLowerLast : String = ""

    override fun doInBackground(vararg params: SheetActivity.SearchData?) {

        val searchData = params[0]

        if (searchData != null) {
            doSearch(searchData.string, searchData.sheet, searchData.lastSearch)
        }
    }

    fun doSearch(string: String, spreadsheet: Spreadsheet, lastSearch : SheetActivity.LastSearch) {
        // case sensitive/insensitive
        // whole/partial word
        val wholeWord = false
        val caseSensitive = false

        val find = string.toLowerCase()

        val workbook = spreadsheet.workbook
        val currentSheet = workbook.currentSheet
        val sheet = workbook.sheetList[currentSheet]

        val startRow : Int
        var startColumn : Int

        var cellValue : String
        var first  = true


        if (lastSearch.word.contains(find)) {
            startRow = lastSearch.row
            startColumn = lastSearch.column + 1
        } else {
            startRow = 0
            startColumn = 0
        }

        for (ri in startRow until sheet.rowList.size) {
            for (ci in startColumn until sheet.rowList[ri].cellList.size){
                cellValue = sheet.rowList[ri].cellList[ci].cellValue.toLowerCase()
                if (first && !wholeWord && !caseSensitive && cellValue.contains(find)) {
                    first = false
                    searchRow = ri
                    searchColumn = ci
                    searchLowerLast = cellValue
                }
            }
            startColumn=0
        }
    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)
        weakContext.get()?.lastSearch = SheetActivity.LastSearch(searchRow, searchColumn, searchLowerLast)
        weakContext.get()?.doSearchJump()
    }

}