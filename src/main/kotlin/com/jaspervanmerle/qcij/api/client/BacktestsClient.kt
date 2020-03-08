package com.jaspervanmerle.qcij.api.client

import com.github.salomonbrys.kotson.fromJson
import com.github.salomonbrys.kotson.jsonObject
import com.jaspervanmerle.qcij.api.APIClient
import com.jaspervanmerle.qcij.api.model.GetAllBacktestsResponse
import com.jaspervanmerle.qcij.api.model.QuantConnectBacktest

class BacktestsClient(private val api: APIClient) {
    fun get(projectId: Int, backtestId: String): QuantConnectBacktest {
        return api.gson.fromJson(api.get("/backtests/read?projectId=$projectId&backtestId=$backtestId"))
    }

    fun getAll(projectId: Int): List<QuantConnectBacktest> {
        return api.gson.fromJson<GetAllBacktestsResponse>(api.get("/backtests/read?projectId=$projectId")).backtests
    }

    fun create(projectId: Int, compileId: String, name: String): QuantConnectBacktest {
        val response = api.post("/backtests/create", jsonObject(
            "projectId" to projectId,
            "compileId" to compileId,
            "backtestName" to name
        ))

        return api.gson.fromJson(response)
    }

    fun updateBacktest(projectId: Int, backtestId: String, name: String, note: String) {
        api.post("/backtests/update", jsonObject(
            "projectId" to projectId,
            "backtestId" to backtestId,
            "name" to name,
            "note" to note
        ))
    }

    fun delete(projectId: Int, backtestId: String) {
        api.post("/backtests/delete?projectId=$projectId&backtestId=$backtestId")
    }
}
