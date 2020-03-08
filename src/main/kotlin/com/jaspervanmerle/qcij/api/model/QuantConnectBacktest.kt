package com.jaspervanmerle.qcij.api.model

import java.util.Date
import org.json.JSONObject

data class QuantConnectBacktest(
    val backtestId: String,
    val name: String,
    val note: String?,
    val created: Date,
    val completed: Boolean,
    val progress: Int,
    val error: String?,
    val stacktrace: String?,

    // The result object can be huge and contains all result data including charting data
    // This property is private and the results property should be used instead as it provides an actual JsonObject
    // Example response containing a full result object with the chart values arrays truncated to a single item:
    // https://gist.github.com/jmerle/c0bf7435cf862803a93a05fe6e41c2a9
    val result: JSONObject?
) {
    val successful = completed && progress == 1 && result?.has("TotalPerformance") == true
}
