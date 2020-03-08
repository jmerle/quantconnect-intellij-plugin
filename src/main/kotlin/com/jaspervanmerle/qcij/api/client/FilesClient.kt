package com.jaspervanmerle.qcij.api.client

import com.github.salomonbrys.kotson.fromJson
import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.Gson
import com.jaspervanmerle.qcij.api.APIClient
import com.jaspervanmerle.qcij.api.model.GetAllFilesResponse

class FilesClient(val api: APIClient) {
    private val gson = Gson()

    fun getAll(projectId: Int): GetAllFilesResponse {
        return gson.fromJson(api.get("/files/read?projectId=$projectId"))
    }

    fun create(projectId: Int, filename: String, content: String) {
        api.post("/files/create", jsonObject(
            "projectId" to projectId,
            "name" to filename,
            "content" to content
        ))
    }

    fun update(projectId: Int, filename: String, content: String) {
        api.post("/files/update", jsonObject(
            "projectId" to projectId,
            "name" to filename,
            "content" to content
        ))
    }

    fun delete(projectId: Int, filename: String) {
        api.post("/files/delete", jsonObject(
            "projectId" to projectId,
            "name" to filename
        ))
    }
}
