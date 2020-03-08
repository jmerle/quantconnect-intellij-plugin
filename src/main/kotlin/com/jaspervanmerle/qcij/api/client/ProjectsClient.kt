package com.jaspervanmerle.qcij.api.client

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.jaspervanmerle.qcij.api.APIClient
import com.jaspervanmerle.qcij.api.model.GetAllProjectsResponse
import com.jaspervanmerle.qcij.api.model.GetProjectResponse

class ProjectsClient(val api: APIClient) {
    private val gson = Gson()

    fun get(projectId: Int): GetProjectResponse {
        return gson.fromJson(api.get("/projects/read?projectId=$projectId"))
    }

    fun getAll(): GetAllProjectsResponse {
        return gson.fromJson(api.get("/projects/read"))
    }
}
