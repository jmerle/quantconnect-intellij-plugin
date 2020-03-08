package com.jaspervanmerle.qcij.api.model

import com.fasterxml.jackson.annotation.JsonProperty

data class GetAllFilesResponse(val files: List<QuantConnectFile>)
data class GetAllProjectsResponse(val projects: List<QuantConnectProject>)
data class GetAllBacktestsResponse(val backtests: List<QuantConnectBacktest>)

data class CreateCompileResponse(
    val projectId: Int,
    val compileId: String,
    val signature: String,
    val state: QuantConnectCompileState,
    @JsonProperty("parameters") val files: List<QuantConnectCompileFile>
)

data class GetCompileResponse(
    val compileId: String,
    val state: QuantConnectCompileState,
    val logs: List<String>
)
