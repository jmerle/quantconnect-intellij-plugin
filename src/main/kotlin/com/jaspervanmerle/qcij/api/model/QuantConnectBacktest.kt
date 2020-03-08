package com.jaspervanmerle.qcij.api.model

import java.util.Date

data class QuantConnectBacktest(
    val backtestId: String,
    val name: String,
    val note: String?,
    val created: Date,
    val completed: Boolean,
    val progress: Int,
    val error: String?,
    val stacktrace: String?
) {
    val successful = completed && progress == 1
}
