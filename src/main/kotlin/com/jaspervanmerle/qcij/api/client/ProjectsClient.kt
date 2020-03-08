package com.jaspervanmerle.qcij.api.client

import com.github.salomonbrys.kotson.fromJson
import com.github.salomonbrys.kotson.jsonObject
import com.jaspervanmerle.qcij.api.APIClient
import com.jaspervanmerle.qcij.api.model.APIException
import com.jaspervanmerle.qcij.api.model.GetAllProjectsResponse
import com.jaspervanmerle.qcij.api.model.QuantConnectProject

class ProjectsClient(private val api: APIClient) {
    fun get(projectId: Int): QuantConnectProject {
        return api.gson
            .fromJson<GetAllProjectsResponse>(api.get("/projects/read?projectId=$projectId"))
            .projects
            .firstOrNull() ?: throw APIException("Project $projectId does not exist")
    }

    fun getAll(): List<QuantConnectProject> {
        return api.gson.fromJson<GetAllProjectsResponse>(api.get("/projects/read")).projects
    }

    fun delete(projectId: Int) {
        api.post("/projects/delete", jsonObject("projectId" to projectId))
    }
}
