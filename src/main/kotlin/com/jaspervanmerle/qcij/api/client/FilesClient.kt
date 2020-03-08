package com.jaspervanmerle.qcij.api.client

import com.github.salomonbrys.kotson.fromJson
import com.github.salomonbrys.kotson.jsonObject
import com.jaspervanmerle.qcij.api.APIClient
import com.jaspervanmerle.qcij.api.model.APIException
import com.jaspervanmerle.qcij.api.model.GetAllFilesResponse
import com.jaspervanmerle.qcij.api.model.QuantConnectFile

class FilesClient(private val api: APIClient) {
    fun get(projectId: Int, filename: String): QuantConnectFile {
        return api.gson
            .fromJson<GetAllFilesResponse>(api.get("/files/read?projectId=$projectId&name=$filename"))
            .files
            .firstOrNull() ?: throw APIException("File $filename in project $projectId does not exist")
    }

    fun getAll(projectId: Int): List<QuantConnectFile> {
        return api.gson.fromJson<GetAllFilesResponse>(api.get("/files/read?projectId=$projectId")).files
    }

    fun create(projectId: Int, filename: String, content: String): QuantConnectFile {
        val response = api.post("/files/create", jsonObject(
            "projectId" to projectId,
            "name" to filename,
            "content" to content
        ))

        return api.gson
            .fromJson<GetAllFilesResponse>(response)
            .files
            .firstOrNull() ?: throw APIException("File $filename in project $projectId could not be created")
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
