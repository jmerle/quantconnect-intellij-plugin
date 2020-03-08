package com.jaspervanmerle.qcij.api.model.response

import com.jaspervanmerle.qcij.api.model.QuantConnectCompileState

data class GetCompileResponse(
    val compileId: String,
    val state: QuantConnectCompileState,
    val logs: List<String>
)
