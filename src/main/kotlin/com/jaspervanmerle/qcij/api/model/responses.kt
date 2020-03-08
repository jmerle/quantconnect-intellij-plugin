package com.jaspervanmerle.qcij.api.model

data class GetAllFilesResponse(val files: List<QuantConnectFile>)

data class GetProjectResponse(private val projects: List<QuantConnectProject>) {
    val project = projects.firstOrNull()
}

data class GetAllProjectsResponse(val projects: List<QuantConnectProject>)
