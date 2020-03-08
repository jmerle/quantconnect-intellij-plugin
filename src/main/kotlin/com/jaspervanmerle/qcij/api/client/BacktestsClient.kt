package com.jaspervanmerle.qcij.api.client

import com.jaspervanmerle.qcij.api.APIClient
import com.jaspervanmerle.qcij.api.model.GetAllBacktestsResponse
import com.jaspervanmerle.qcij.api.model.QuantConnectBacktest
import org.json.JSONObject

class BacktestsClient(private val api: APIClient) {
    fun get(projectId: Int, backtestId: String): QuantConnectBacktest {
        return api.klaxon.parse(api.get("/backtests/read?projectId=$projectId&backtestId=$backtestId"))!!
    }

    fun getAll(projectId: Int): List<QuantConnectBacktest> {
        return api.klaxon.parse<GetAllBacktestsResponse>(api.get("/backtests/read?projectId=$projectId"))!!.backtests
    }

    fun create(projectId: Int, compileId: String, name: String): QuantConnectBacktest {
        val response = api.post("/backtests/create", JSONObject(mapOf(
            "projectId" to projectId,
            "compileId" to compileId,
            "backtestName" to name
        )))

        return api.klaxon.parse(response)!!
    }

    fun update(projectId: Int, backtestId: String, name: String, note: String) {
        api.post("/backtests/update", JSONObject(mapOf(
            "projectId" to projectId,
            "backtestId" to backtestId,
            "name" to name,
            "note" to note
        )))
    }

    fun delete(projectId: Int, backtestId: String) {
        api.post("/backtests/delete?projectId=$projectId&backtestId=$backtestId")
    }
}
