package com.jaspervanmerle.qcij.api.model.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.jaspervanmerle.qcij.api.model.QuantConnectCompileFile
import com.jaspervanmerle.qcij.api.model.QuantConnectCompileState

data class CreateCompileResponse(
    val projectId: Int,
    val compileId: String,
    val signature: String,
    val state: QuantConnectCompileState,
    @JsonProperty("parameters") val files: List<QuantConnectCompileFile>
)
