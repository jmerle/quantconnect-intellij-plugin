package com.jaspervanmerle.qcij.api.model

data class GetAllFilesResponse(val files: List<QuantConnectFile>)
data class GetAllProjectsResponse(val projects: List<QuantConnectProject>)
data class GetAllBacktestsResponse(val backtests: List<QuantConnectBacktest>)
