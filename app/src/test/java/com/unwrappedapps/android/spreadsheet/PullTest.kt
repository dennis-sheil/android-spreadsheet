package com.unwrappedapps.android.spreadsheet

import org.junit.Test
import org.junit.Assert.*

class PullTest {
    @Test
    fun pullstates_check() {
        val pullStateLoading = PullState.LOADING
        val pullStateLoadedAck = PullState.LOADED_ACK
        assertEquals(pullStateLoading, PullState.LOADING)
        assertNotEquals(pullStateLoading, pullStateLoadedAck)
    }
}
