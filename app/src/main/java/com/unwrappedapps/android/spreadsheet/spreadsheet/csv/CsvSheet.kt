package com.unwrappedapps.android.spreadsheet.spreadsheet.csv

import com.unwrappedapps.android.spreadsheet.spreadsheet.Sheet
import java.io.InputStream

class CsvSheet(inputStream: InputStream) : Sheet() {

    companion object {
        const val END_OF_STREAM = -1
    }

    init {

        var c: Char
        try {
            var lineSoFar = ""
            var nextByte = inputStream.read()
            while (nextByte != END_OF_STREAM) {
                c = nextByte.toChar()
                if (c == '\n') {
                    val csvRow = CsvRow(lineSoFar)
                    rowList.add(csvRow)
                    lineSoFar = ""
                } else {
                    lineSoFar = lineSoFar + c
                }
                nextByte = inputStream.read()
            }
            inputStream.close()
        } catch (e: Exception) {
            //Log.d("csvsheet", "error $e")
        }
    }
}
