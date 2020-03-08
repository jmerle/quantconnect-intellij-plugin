package com.jaspervanmerle.qcij.api.client

import com.fasterxml.jackson.module.kotlin.readValue
import com.jaspervanmerle.qcij.api.APIClient
import com.jaspervanmerle.qcij.api.model.APIException
import com.jaspervanmerle.qcij.api.model.GetAllProjectsResponse
import com.jaspervanmerle.qcij.api.model.QuantConnectProject

class ProjectClient(private val api: APIClient) {
    fun get(projectId: Int): QuantConnectProject {
        return api.objectMapper
            .readValue<GetAllProjectsResponse>(api.get("/projects/read?projectId=$projectId"))
            .projects
            .firstOrNull() ?: throw APIException("Project $projectId does not exist")
    }

    fun getAll(): List<QuantConnectProject> {
        return api.objectMapper
            .readValue<GetAllProjectsResponse>(api.get("/projects/read"))
            .projects
    }

    fun delete(projectId: Int) {
        api.post("/projects/delete", mapOf("projectId" to projectId))
    }

    fun addLibrary(projectId: Int, libraryId: Int) {
        api.post("/projects/library/create", mapOf(
            "projectId" to projectId,
            "libraryId" to libraryId
        ))
    }

    fun removeLibrary(projectId: Int, libraryId: Int) {
        api.post("/projects/library/delete", mapOf(
            "projectId" to projectId,
            "libraryId" to libraryId
        ))
    }
}
