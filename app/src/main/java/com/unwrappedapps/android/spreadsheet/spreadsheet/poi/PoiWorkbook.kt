package com.unwrappedapps.android.spreadsheet.spreadsheet.poi

import com.unwrappedapps.android.spreadsheet.spreadsheet.Workbook
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream

open class PoiWorkbook (inputStream: InputStream): Workbook() {

    init {

        // hssf/xssf
        val workbook : org.apache.poi.ss.usermodel.Workbook = WorkbookFactory.create(inputStream)

        val poiSheet = workbook.getSheetAt(0)

        val pSheet : PoiSheet

        pSheet = PoiSheet(poiSheet)

        if (sheetList.size > 1) {
            sheetList.clear()
        }

        sheetList.set(0, pSheet)

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
