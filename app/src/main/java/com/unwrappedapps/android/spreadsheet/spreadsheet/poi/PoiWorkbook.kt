package com.unwrappedapps.android.spreadsheet.spreadsheet.poi

import com.unwrappedapps.android.spreadsheet.spreadsheet.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream

open class PoiWorkbook (inputStream: InputStream): Workbook() {

    init {
        // hssf/xssf
        val workbook : org.apache.poi.ss.usermodel.Workbook = WorkbookFactory.create(inputStream)

        sheetList.clear()

        for (i in 0 until workbook.numberOfSheets) {
            val poiSheet = workbook.getSheetAt(i)
            val pSheet : PoiSheet
            pSheet = PoiSheet(poiSheet)
            sheetList.add(pSheet)
        }
    }

    companion object {
        init {
            System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLInputFactory",
                "com.fasterxml.aalto.stax.InputFactoryImpl"
            )
            System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLOutputFactory",
                "com.fasterxml.aalto.stax.OutputFactoryImpl"
            )
            System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLEventFactory",
                "com.fasterxml.aalto.stax.EventFactoryImpl"
            )
        }
    }

}
