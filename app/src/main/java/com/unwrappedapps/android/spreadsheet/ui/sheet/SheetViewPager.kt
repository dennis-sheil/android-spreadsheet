package com.unwrappedapps.android.spreadsheet.ui.sheet

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class SheetViewPager : ViewPager {

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {

    }

    // no swiping
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

}