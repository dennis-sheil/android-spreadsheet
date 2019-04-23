package com.unwrappedapps.android.spreadsheet
enum class Pull {
    RUNNING,
    SUCCESSACK,
    SUCCESSNAK,
    FAILED
}

@Suppress("DataClassPrivateConstructor")
data class PullState private constructor(
    val pull: Pull,
    val msg: String? = null) {
    companion object {
        val LOADED_ACK = PullState(Pull.SUCCESSACK)
        val LOADED_NAK = PullState(Pull.SUCCESSNAK)
        val LOADING = PullState(Pull.RUNNING)
        fun error(msg: String?) = PullState(Pull.FAILED, msg)
    }
}
