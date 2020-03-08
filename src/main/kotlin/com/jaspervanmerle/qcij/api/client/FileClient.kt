package com.jaspervanmerle.qcij.api.client

import com.fasterxml.jackson.module.kotlin.readValue
import com.jaspervanmerle.qcij.api.APIClient
import com.jaspervanmerle.qcij.api.model.APIException
import com.jaspervanmerle.qcij.api.model.GetAllFilesResponse
import com.jaspervanmerle.qcij.api.model.QuantConnectFile
import org.json.JSONObject

class FileClient(private val api: APIClient) {
    fun get(projectId: Int, filename: String): QuantConnectFile {
        return api.objectMapper
            .readValue<GetAllFilesResponse>(api.get("/files/read?projectId=$projectId&name=$filename"))
            .files
            .firstOrNull() ?: throw APIException("File $filename in project $projectId does not exist")
    }

    fun getAll(projectId: Int): List<QuantConnectFile> {
        return api.objectMapper
            .readValue<GetAllFilesResponse>(api.get("/files/read?projectId=$projectId"))
            .files
    }

    fun create(projectId: Int, filename: String, content: String): QuantConnectFile {
        val response = api.post("/files/create", JSONObject(mapOf(
            "projectId" to projectId,
            "name" to filename,
            "content" to content
        )))

        return api.objectMapper
            .readValue<GetAllFilesResponse>(response)
            .files
            .firstOrNull() ?: throw APIException("File $filename in project $projectId could not be created")
    }

    fun update(projectId: Int, filename: String, content: String) {
        api.post("/files/update", JSONObject(mapOf(
            "projectId" to projectId,
            "name" to filename,
            "content" to content
        )))
    }

    fun delete(projectId: Int, filename: String) {
        api.post("/files/delete", JSONObject(mapOf(
            "projectId" to projectId,
            "name" to filename
        )))
    }
}
