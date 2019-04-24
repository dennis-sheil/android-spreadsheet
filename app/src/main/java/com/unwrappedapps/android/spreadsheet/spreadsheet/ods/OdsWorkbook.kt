package com.unwrappedapps.android.spreadsheet.spreadsheet.ods

import android.util.Log
import android.util.Xml
import com.unwrappedapps.android.spreadsheet.spreadsheet.Sheet
import com.unwrappedapps.android.spreadsheet.spreadsheet.Workbook
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.util.jar.JarInputStream


/*

TODO: This is a very bare bones ODS implementation.
Either fill it out or look for an appropriately
licensed library to use for it (or make one).

 */

class OdsWorkbook(inputStream : InputStream) : Workbook() {


    init {
        sheetList = OdsCreator(inputStream).getList()
    }


    class OdsCreator(inputStream : InputStream)  {

        private val ns: String? = null
        private var helperSheetList: MutableList<Sheet> = mutableListOf()

        init {
            helperSheetList = mutableListOf()
            makeOds(inputStream)
        }


        private fun makeOds(inputStream: InputStream) {
            try {
                val jis = JarInputStream(inputStream)
                var je = jis.getNextJarEntry()

                while (je != null) {

                    if (je.getName().equals("content.xml")) {
                        parse(jis)
                    }

                    je = jis.getNextJarEntry()

                }
                jis.close()
                inputStream.close()
            } catch (e: Exception) {
                Log.d("makeods", e.toString())
            }


        }

        @Throws(XmlPullParserException::class, IOException::class)
        fun parse(inputStream: InputStream): List<*> {
            try {
                val parser = Xml.newPullParser()
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                parser.setInput(inputStream, null)
                parser.nextTag()
                return readFeed(parser)
            } finally {
                inputStream.close()
            }
        }


        @Throws(XmlPullParserException::class, IOException::class)
        private fun readFeed(parser: XmlPullParser): List<*> {
            val body = mutableListOf<String>()
            parser.require(XmlPullParser.START_TAG, ns, "office:document-content")
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                val name = parser.name

                // Starts by looking for the entry tag

                if (name == "office:body") {
                    body.add(readBody(parser))  // N.B. see readBody comments
                    break
                } else {
                    skip(parser)
                }

            }

            return body
        }

        @Throws(XmlPullParserException::class, IOException::class)
        private fun skip(parser: XmlPullParser) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                throw IllegalStateException()
            }
            var depth = 1
            while (depth != 0) {
                when (parser.next()) {
                    XmlPullParser.END_TAG -> depth--
                    XmlPullParser.START_TAG -> depth++
                }
            }
        }


        // TODO - this function is not really returning anything
        // clean up later
        @Throws(XmlPullParserException::class, IOException::class)
        private fun readBody(parser: XmlPullParser) : String {

            parser.require(XmlPullParser.START_TAG, ns, "office:body")
            while (parser.next() != XmlPullParser.END_TAG) {

                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                val name = parser.name

                if (name == "office:spreadsheet") {
                    readSpreadsheet(parser)
                    break
                } else {
                    skip(parser)
                }

            }
            return "filler" // N.B. - bogus body return
        }

        @Throws(XmlPullParserException::class, IOException::class)
        private fun readSpreadsheet(parser: XmlPullParser) {

            parser.require(XmlPullParser.START_TAG, ns, "office:spreadsheet")

            var pn = parser.next()

            while (pn != XmlPullParser.END_TAG) {

                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                val name = parser.name

                val odsTable: OdsSheet

                if (name == "table:table") {

                    val s = checkForAttribute(parser, "table:name")

                    odsTable = readTable(parser)

                    if (s.length > 0) {
                        odsTable.name = s
                    }

                    helperSheetList.add(odsTable)


                } else {
                    skip(parser)
                }

                pn = parser.next()

            }

        }

        // table or sheet
        @Throws(XmlPullParserException::class, IOException::class)
        private fun readTable(parser: XmlPullParser): OdsSheet {

            val odsTable = OdsSheet()
            var odsRow: OdsRow

            parser.require(XmlPullParser.START_TAG, ns, "table:table")
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                val name = parser.name

                if (name == "table:table-row") {

                    val repeatNum = checkForRepeats(parser, "table:number-rows-repeated")

                    odsRow = readTableRow(parser)
                    odsTable.add(odsRow)
                    if (repeatNum > 0) {
                        for (i in 1 until repeatNum) {
                            odsTable.add(odsRow)
                        }
                    }


                } else if (name == "table:table-header-rows") {
                    odsRow = readTableHeaderRows(parser)
                    odsTable.add(odsRow)
                } else {
                    skip(parser)
                }
            }
            return odsTable
        }

        @Throws(XmlPullParserException::class, IOException::class)
        private fun readTableHeaderRows(parser: XmlPullParser): OdsRow {

            parser.require(XmlPullParser.START_TAG, ns, "table:table-header-rows")

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                val name = parser.name

                if (name == "table:table-row") {

                    return readTableRow(parser)
                } else {
                    skip(parser)
                }
            }
            return OdsRow()
        }


        @Throws(XmlPullParserException::class, IOException::class)
        private fun readTableRow(parser: XmlPullParser): OdsRow {

            val odsRow = OdsRow()
            var odsCell: OdsCell

            parser.require(XmlPullParser.START_TAG, ns, "table:table-row")
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                val name = parser.name

                if (name == "table:table-cell") {

                    val repeatNum = checkForRepeats(parser, "table:number-columns-repeated")

                    odsCell = readTableCell(parser)
                    odsRow.add(odsCell)

                    if (repeatNum > 0) {
                        for (i in 1 until repeatNum) {
                            odsRow.add(odsCell)
                        }
                    }

                } else {
                    skip(parser)
                }
            }
            return odsRow

        }

        fun checkForRepeats(parser: XmlPullParser, attribute: String): Int {

            val r = checkForAttribute(parser, attribute)
            return if (r.length > 0) {
                r.toInt()
            } else {
                -1
            }
        }

        fun checkForAttribute(parser: XmlPullParser, attribute: String): String {
            val count = parser.attributeCount
            var r = ""
            for (i in 0 until count) {
                val names = parser.getAttributeName(i)
                if (names == attribute) {
                    r = parser.getAttributeValue(i)
                }
            }
            return r

        }


        @Throws(XmlPullParserException::class, IOException::class)
        private fun readTableCell(parser: XmlPullParser): OdsCell {

            var textP = ""

            parser.require(XmlPullParser.START_TAG, ns, "table:table-cell")

            while (parser.next() != XmlPullParser.END_TAG) {

                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                val name = parser.name

                if (name == "text:p") {
                    textP = readTextP(parser)
                } else {
                    skip(parser)
                }
            }

            return OdsCell(textP)
        }

        @Throws(XmlPullParserException::class, IOException::class)
        private fun readTextP(parser: XmlPullParser): String {

            parser.require(XmlPullParser.START_TAG, ns, "text:p")

            var s = ""

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType == 4) {

                    // TODO: Doing first one, handle others in future
                    if (s.length < 1) {
                        s = parser.text
                    }

                } else {
                    skip(parser)
                }
            }

            return s
        }


        fun getList(): MutableList<Sheet> {

            return helperSheetList
        }
    }

}