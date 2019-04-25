package com.unwrappedapps.android.spreadsheet.ui.sheet

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SheetLayoutManager : RecyclerView.LayoutManager() {

    companion object {
        // need to know if we should stop scrolling up and left, to begin with
        var leftColumn = 1
        var topRow = 1

        private const val HEADER_COLUMN: Int = 0
        private const val HEADER_ROW: Int = 0
        private const val CELL : String = "cell"
        private const val ROW : String = "row"
        private const val COLUMN : String = "column"
        private const val TOP_LEFT : String = "topLeft"

        private var skipLayoutForSearch : Boolean = false

        enum class Tilt{ VERTICAL, HORIZONTAL }
        enum class Side{ ROWS, COLUMNS }
        enum class Direction{ LIMITED, UNLIMITED }
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?,
                                  state: RecyclerView.State?) {
        doInitialLayout(recycler)
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        handleScroll(dx, recycler, Tilt.HORIZONTAL)
        return dx
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        handleScroll(dy, recycler, Tilt.VERTICAL)
        return dy
    }

    override fun canScrollHorizontally() = true

    override fun canScrollVertically() = true

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
    }


    // mark when search done to prevent onLayoutChildren call
    // this may need to be made more complex than a boolean
    fun startSearch() {
        skipLayoutForSearch = true
    }

    fun resetLayoutManagerSearch() {
        skipLayoutForSearch = false
    }

    fun resetToTopLeft() {
        leftColumn = 1
        topRow = 1
    }

    private fun doInitialLayout(recycler: RecyclerView.Recycler?) {

        if (skipLayoutForSearch) {
            return
        }

        val jumpColumn = leftColumn
        val jumpRow = topRow

        var currentColumn = HEADER_COLUMN
        var currentRow = HEADER_ROW
        var cellView : View?
        var filledWidth = 0
        var filledHeight = 0
        var cellHeight = 0
        var columnInc = 0
        var rowInc = 0

        if (recycler != null) {
            removeAndRecycleAllViews(recycler)
            detachAndScrapAttachedViews(recycler)
        }

        while (height - filledHeight >= 0) {
            while (width - filledWidth >= 0) {
                cellView = layoutChildView(recycler, currentColumn, currentRow, filledWidth, filledHeight, Direction.UNLIMITED, Tilt.VERTICAL)
                filledWidth = filledWidth + getDecoratedSide(cellView, Tilt.VERTICAL)
                currentColumn = jumpColumn + columnInc++
                cellHeight = getDecoratedSide(cellView, Tilt.HORIZONTAL) // we don't need to measure this each time
            }

            currentColumn = HEADER_COLUMN
            columnInc = 0
            filledWidth = 0
            filledHeight = filledHeight + cellHeight
            currentRow = jumpRow + rowInc++
        }
    }


    // TODO: think of more elegant solution
    private fun getDecoratedMeasuredNullableWidth (cellView : View?) : Int =
        if (cellView == null) 0
        else getDecoratedMeasuredWidth(cellView)

    // TODO: think of more elegant solution
    private fun getDecoratedMeasuredNullableHeight (cellView : View?) : Int =
        if (cellView == null) 0
        else getDecoratedMeasuredHeight(cellView)


    private fun layoutChildView(recycler: RecyclerView.Recycler?,
                          column: Int, row: Int,
                          fWidth: Int, fHeight: Int,
                          direction: Direction, tilt: Tilt): View? {

        val position = markerToPos(row, column)
        val cellView = recycler?.getViewForPosition(position) ?: return null

        // TODO: This call is needed for a newly loaded file, not for a rotation etc.
        // so it can be called less
        recycler.bindViewToPosition(cellView,position)

        addView(cellView)
        measureChildWithMargins(cellView, 0, 0)
        val cellWidth = getDecoratedMeasuredWidth(cellView)
        val cellHeight = getDecoratedMeasuredHeight(cellView)

        when {
            direction == Direction.UNLIMITED ->
                layoutDecorated(cellView, fWidth, fHeight, cellWidth + fWidth, cellHeight + fHeight)
            tilt == Tilt.VERTICAL ->
                layoutDecorated(cellView, fWidth, fHeight - cellHeight, cellWidth + fWidth, fHeight)
            else ->  // LIMITED HORIZONTAL (right to left)
                layoutDecorated(cellView, fWidth - cellWidth, fHeight, fWidth, cellHeight + fHeight)
        }

        setCellType(cellView, row, column)

        return cellView
    }


    private fun markerToPos(r: Int, c: Int): Int {
        val t = c + r + 1
        return t * (t + 1) / 2 - c - 1
    }

    // row and column headers get scrolled under, top left header scrolled under by all
    private fun setCellType(cellView: View?, row: Int, column: Int) {
        if (row == 0 || column == 0) {
            if (row == 0 && column == 0) {
                cellView?.z = 1.2f
                cellView?.tag = TOP_LEFT
            } else {
                cellView?.z = 1.1f
                if (column == 0) {
                    cellView?.tag = COLUMN
                }
                if (row == 0) {
                    cellView?.tag = ROW
                }
            }
        } else {
            cellView?.tag = CELL
        }
    }


    private fun handleScroll(d: Int, recycler: RecyclerView.Recycler?, tilt : Tilt) {

        val direction : Direction
        var dd : Int

        direction = if (d < 0) Direction.LIMITED else Direction.UNLIMITED

        if (direction == Direction.LIMITED) {
            dd=d*-1
            do {
                dd = processLimitedScroll(dd, recycler, tilt)
                scrapOffscreenViews(recycler, direction, tilt)
            } while (dd > 0 && ((topRow > 1 && tilt == Tilt.VERTICAL) || (leftColumn > 1 && tilt == Tilt.HORIZONTAL)))
        } else {
            dd=d
            do {
                dd = processUnlimitedScroll(dd, recycler, tilt)
                scrapOffscreenViews(recycler, direction, tilt)
            } while (dd > 0)
        }
    }


    // unlimited - do not need to worry about scrolling past row 1, column A row/column markers
    private fun processUnlimitedScroll(scrollSize: Int, recycler: RecyclerView.Recycler?, tilt: Tilt) : Int {

        val orientation = if (tilt == Tilt.VERTICAL) COLUMN else ROW

        val farthestOut = getFarthest(tilt)

        val breadth = if (tilt == Tilt.VERTICAL) height else width
        val amountBeyondScreen = farthestOut - breadth

        if (amountBeyondScreen > scrollSize) {
            offsetCells(scrollSize, orientation)
        } else {
            offsetCells(amountBeyondScreen, orientation)
            newMarker(recycler, Direction.UNLIMITED, tilt)
            var remainingMove = scrollSize - amountBeyondScreen
            return remainingMove
        }
        return 0
    }

    // limited - need to worry about scrolling past row 1, column A row/column markers
    private fun processLimitedScroll(scrollSize: Int, recycler: RecyclerView.Recycler?, tilt : Tilt) : Int {

        val orientation = if (tilt == Tilt.VERTICAL) COLUMN else ROW

        val ltd = getLimited(tilt)

        if (ltd >= scrollSize) { // or >
            offsetCells(-scrollSize, orientation)
        } else if (topRow <= 1 && tilt == Tilt.VERTICAL || leftColumn <= 1 && tilt == Tilt.HORIZONTAL) {
            offsetCells(-ltd, orientation)
        } else {
            val rowSide = newMarker(recycler, Direction.LIMITED, tilt)
            val remainSize = rowSide - scrollSize
            return remainSize*-1
        }

        return 0
    }


    private fun newMarker(recycler: RecyclerView.Recycler?, direction: Direction, tilt: Tilt): Int {

        val screenBreadth: Int
        val screenLimit: Int
        val cellView: View?
        val currentColumn: Int
        val currentRow: Int

        if (tilt == Tilt.VERTICAL) {

            if (direction == Direction.LIMITED) {
                topRow--
                currentRow = topRow
                screenLimit = getSmallest(tilt)

            } else {
                currentRow = getSideCount(Side.ROWS) + topRow
                screenLimit = height
            }

            currentColumn = leftColumn
            screenBreadth = width
            cellView = layoutChildView(recycler, 0, currentRow, 0, screenLimit, direction, tilt)

        } else {

            if (direction == Direction.LIMITED) {
                leftColumn--
                currentColumn = leftColumn
                screenLimit = getSmallest(tilt)
            } else {
                currentColumn = getSideCount(Side.COLUMNS) + leftColumn
                screenLimit = width
            }

            currentRow = topRow
            screenBreadth = height
            cellView = layoutChildView(recycler, currentColumn, 0, screenLimit, 0, direction, tilt)
        }

        val perpendicularTilt = if (tilt == Tilt.VERTICAL) Tilt.HORIZONTAL else Tilt.VERTICAL
        val cellBreadth = getDecoratedSide(cellView, perpendicularTilt)
        val offset = getLimited(perpendicularTilt) * -1
        val filled = offset + getDecoratedSide(cellView, tilt)

        fillNewSide(screenBreadth, filled, tilt, currentColumn, currentRow, recycler, screenLimit, direction)

        return cellBreadth
    }


    private fun fillNewSide(screenBreadth : Int, initialFilled : Int, tilt: Tilt,
            initialColumn : Int, initialRow : Int,
            recycler : RecyclerView.Recycler?,
            screenLimit : Int, direction: Direction) {

        var cellView : View?
        var filled = initialFilled
        var currentColumn = initialColumn
        var currentRow = initialRow

        // do subsequent
        while (screenBreadth - filled >= 0) {

            cellView = if (tilt == Tilt.VERTICAL)
                layoutChildView(recycler, currentColumn, currentRow, filled, screenLimit, direction, tilt)
            else layoutChildView(recycler, currentColumn, currentRow, screenLimit, filled, direction, tilt)

            filled = filled + getDecoratedSide(cellView, tilt)
            if (tilt == Tilt.VERTICAL) currentColumn++
            else currentRow++
        }

    }


    private fun getDecoratedSide(child : View?, tilt: Tilt) =
        if (tilt == Tilt.HORIZONTAL) getDecoratedMeasuredNullableHeight(child)
        else getDecoratedMeasuredNullableWidth(child)


    private fun getLimited(tilt : Tilt) : Int {
        val smallestSoFar = getSmallest(tilt)

        val topLeft = if (tilt == Tilt.VERTICAL) getChildAt(0)?.bottom ?: 0
        else getChildAt(0)?.right ?: 0

        val sm = topLeft + (smallestSoFar * -1)
        return sm
    }


    private fun getSmallest(tilt : Tilt) : Int {
        var smallestSoFar = 2140000000

        for (i in 0 until childCount) {
            val child = getChildAt(i) ?: continue
            if (cellIsMarker(child)) continue

            val current = if (tilt == Tilt.VERTICAL) child.top
            else child.left

            if (current < smallestSoFar) {
                smallestSoFar = current
            }
        }

        return smallestSoFar
    }


    private fun getFarthest(tilt: Tilt): Int {
        var biggestSoFar = -1

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child == null) continue

            val current = if (tilt == Tilt.VERTICAL) child.bottom else child.right

            if (current > biggestSoFar) {
                biggestSoFar = current
            }
        }
        return biggestSoFar
    }


    private fun offsetCells(d : Int, tag: String) {
        for (i in 1 until childCount) {
            val child = getChildAt(i)

            if (child?.tag == CELL || child?.tag == tag) {
                when (tag) {
                    COLUMN -> child.offsetTopAndBottom(-d)
                    ROW -> child.offsetLeftAndRight(-d)
                }
            }
        }
    }


    // XXX: maybe scrap within scroll
    // maybe increase height/depth
    // maybe relayout screen when shifting directions
    // TODO: scrapping is probably premature, maybe don't scrap until whenever, and
    // send the information to getrows

    private fun scrapOffscreenViews(recycler: RecyclerView.Recycler?, direction : Direction, tilt: Tilt) {

        val initialRows = getSideCount(Side.ROWS)
        val initialColumns = getSideCount(Side.COLUMNS) // new

        if (recycler == null) return

        var bottomCellsGone = 0
        var rightCellsGone = 0

        // detaching in the other direction causes problems
        for (i in childCount-1 downTo 0) {

            val c = getChildAt(i)

            if (c == null) continue

            if (direction == Direction.LIMITED) {
                if ((c.top > height && tilt == Tilt.VERTICAL) ||
                    (c.left > width && tilt == Tilt.HORIZONTAL)) {
                    dealWithView(recycler, c, tilt, direction)
                }
            }
            else {
                if (c.bottom < 0 && tilt == Tilt.VERTICAL) {
                    bottomCellsGone++
                    dealWithView(recycler, c, tilt, direction)
                }

                if (c.right < 0 && tilt == Tilt.HORIZONTAL) {
                    rightCellsGone++
                    dealWithView(recycler, c, tilt, direction)
                }
            }
        }

        sideAdjust(direction, initialRows, initialColumns, bottomCellsGone, rightCellsGone)
    }

    // TODO: considering recycling offscreen markers
    private fun dealWithView(recycler: RecyclerView.Recycler, c : View, tilt:Tilt, direction: Direction) {
        detachAndScrapView(c, recycler)
    }

    private fun sideAdjust(direction: Direction, initialRows: Int, initialColumns: Int,
                   bottomCellsGone: Int, rightCellsGone: Int) {

        val rowDiff : Int
        val columnDiff : Int
        if (direction == Direction.UNLIMITED) {
            val rowsEnd = getSideCount(Side.ROWS)
            val columnsEnd = getSideCount(Side.COLUMNS)
            rowDiff = initialRows - rowsEnd
            columnDiff = initialColumns - columnsEnd
        } else if (initialColumns > 0 && initialRows > 0){
            rowDiff = bottomCellsGone / initialColumns
            columnDiff = rightCellsGone / initialRows
        } else {
            rowDiff = 0
            columnDiff = 0
        }
        topRow = topRow + rowDiff
        leftColumn = leftColumn + columnDiff

    }


    private fun cellIsMarker(child: View) : Boolean {
        return child.z > 1.0f
    }

    private fun getSideCount(side: Side): Int {
        // we add it to a set as it needs uniqueness
        val cells = mutableSetOf<Int>()
        for (i in 0 until childCount) {
            val child = getChildAt(i) ?: continue
            if (cellIsMarker(child)) continue

            val mark = if (side == Side.ROWS) child.top
            else child.left

            cells.add(mark)
        }
        return cells.size
    }

    private class LayoutParams(width: Int, height: Int) :
        RecyclerView.LayoutParams(width, height)

}
