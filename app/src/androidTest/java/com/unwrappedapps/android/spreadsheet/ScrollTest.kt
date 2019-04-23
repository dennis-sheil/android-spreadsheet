package com.unwrappedapps.android.spreadsheet

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ScrollTest {

    // added JvmField annotation because Kotlin seems to want it...
    @Rule
    @JvmField
    //var mActivityRule: ActivityTestRule<TabActivity> = ActivityTestRule(TabActivity::class.java)

    var mActivityRule : ActivityTestRule<SheetActivity> = ActivityTestRule(SheetActivity::class.java)

    @Test
    fun swipeAgain() {
        onView(withId(R.id.container)).perform(swipeLeft())
        onView(withId(R.id.container)).perform(swipeLeft())
        onView(withId(R.id.container)).perform(swipeLeft())
        onView(withId(R.id.container)).perform(swipeLeft())
        onView(withId(R.id.container)).perform(swipeLeft())
        onView(withId(R.id.container)).perform(swipeLeft())
        onView(withId(R.id.container)).perform(swipeLeft())
        onView(withId(R.id.container)).perform(swipeUp())
        onView(withId(R.id.container)).perform(swipeUp())
        onView(withId(R.id.container)).perform(swipeUp())
        onView(withId(R.id.container)).perform(swipeUp())
        onView(withId(R.id.container)).perform(swipeUp())
        onView(withId(R.id.container)).perform(swipeUp())
        onView(withId(R.id.container)).perform(swipeUp())
        onView(withId(R.id.container)).perform(swipeUp())
        onView(withId(R.id.container)).perform(swipeRight())
        onView(withId(R.id.container)).perform(swipeRight())
        onView(withId(R.id.container)).perform(swipeDown())
        onView(withId(R.id.container)).perform(swipeDown())
//        onView(withId(R.id.container)).perform(swipeLeft())
        onView(withId(R.id.container)).perform(longClick())
        onView(withId(R.id.container)).perform(longClick())
        onView(withId(R.id.container)).perform(longClick())
        onView(withId(R.id.container)).perform(longClick())
        onView(withId(R.id.container)).perform(longClick())
        onView(withId(R.id.container)).perform(longClick())
        onView(withId(R.id.container)).perform(longClick())
        onView(withId(R.id.container)).perform(longClick())

    }


/*
    @Test
    fun swipeIt() {
        onView(withId(R.id.container)).perform(swipeLeft())
        onView(withId(R.id.container)).perform(swipeLeft())
        onView(withId(R.id.container)).perform(swipeLeft())
        onView(withId(R.id.container)).perform(swipeLeft())
        onView(withId(R.id.container)).perform(swipeUp())
        onView(withId(R.id.container)).perform(swipeUp())
        onView(withId(R.id.container)).perform(swipeUp())
        onView(withId(R.id.container)).perform(swipeUp())
        onView(withId(R.id.container)).perform(swipeUp())
        onView(withId(R.id.container)).perform(swipeUp())
        onView(withId(R.id.container)).perform(swipeUp())
        onView(withId(R.id.container)).perform(swipeUp())
        onView(withId(R.id.container)).perform(swipeRight())
        onView(withId(R.id.container)).perform(swipeRight())
        onView(withId(R.id.container)).perform(swipeRight())
        onView(withId(R.id.container)).perform(swipeRight())
        onView(withId(R.id.container)).perform(swipeRight())
        onView(withId(R.id.container)).perform(swipeLeft())
        onView(withId(R.id.container)).perform(swipeLeft())
        onView(withId(R.id.container)).perform(swipeLeft())
        onView(withId(R.id.container)).perform(swipeLeft())
        onView(withId(R.id.container)).perform(swipeDown())
        onView(withId(R.id.container)).perform(swipeDown())
        onView(withId(R.id.container)).perform(swipeUp())
        onView(withId(R.id.container)).perform(swipeUp())
        onView(withId(R.id.container)).perform(swipeRight())
        onView(withId(R.id.container)).perform(swipeRight())
        onView(withId(R.id.container)).perform(swipeLeft())
        onView(withId(R.id.container)).perform(swipeDown())
        onView(withId(R.id.container)).perform(swipeUp())
        onView(withId(R.id.container)).perform(swipeUp())
        onView(withId(R.id.container)).perform(swipeRight())
        onView(withId(R.id.container)).perform(swipeRight())
        onView(withId(R.id.container)).perform(click())
        onView(withId(R.id.container)).perform(longClick())
        onView(withId(R.id.container)).perform(longClick())
        onView(withId(R.id.container)).perform(longClick())
        onView(withId(R.id.container)).perform(longClick())
        onView(withId(R.id.container)).perform(longClick())
        onView(withId(R.id.container)).perform(longClick())
        onView(withId(R.id.container)).perform(longClick())
        onView(withId(R.id.container)).perform(longClick())
    }
    */


}