package com.jaspervanmerle.qcij.api.client

import com.fasterxml.jackson.module.kotlin.readValue
import com.jaspervanmerle.qcij.api.APIClient
import com.jaspervanmerle.qcij.api.model.CreateCompileResponse
import com.jaspervanmerle.qcij.api.model.GetCompileResponse

class CompileClient(private val api: APIClient) {
    fun get(projectId: Int, compileId: String): GetCompileResponse {
        return api.objectMapper.readValue(api.get("compile/read?projectId=$projectId&compileId=$compileId"))
    }

    fun create(projectId: Int): CreateCompileResponse {
        return api.objectMapper.readValue(api.post("compile/create?projectId=$projectId"))
    }
}
