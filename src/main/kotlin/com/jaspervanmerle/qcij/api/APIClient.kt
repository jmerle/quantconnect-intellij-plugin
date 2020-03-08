package com.jaspervanmerle.qcij.api

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.result.Result
import com.github.salomonbrys.kotson.array
import com.github.salomonbrys.kotson.bool
import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.string
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.jaspervanmerle.qcij.api.model.APIException
import com.jaspervanmerle.qcij.api.model.InvalidCredentialsException
import com.jaspervanmerle.qcij.api.model.QuantConnectCredentials
import org.apache.commons.codec.digest.DigestUtils

class APIClient(var credentials: QuantConnectCredentials? = null) {
    companion object {
        const val BASE_URL = "https://www.quantconnect.com/api/v2"
    }

    val gson = GsonBuilder()
        .setPrettyPrinting()
        .setDateFormat("yyyy-LL-dd HH:mm:ss")
        .create()

    private val parser = JsonParser()

    fun get(endpoint: String): String {
        return executeRequest(request(Method.GET, endpoint))
    }

    fun post(endpoint: String, body: JsonObject = JsonObject()): String {
        return executeRequest(request(Method.POST, endpoint).body(body.toString()))
    }

    private fun request(method: Method, endpoint: String): Request {
        val request = Fuel.request(method, BASE_URL + endpoint)

        if (credentials == null) {
            return request
        }

        val userId = credentials!!.userId
        val apiToken = credentials!!.apiToken

        val timestamp = System.currentTimeMillis() / 1000
        val hash = DigestUtils.sha256Hex("$apiToken:$timestamp")

        return request
            .authentication()
            .basic(userId, hash)
            .header("Timestamp", timestamp)
    }

    private fun executeRequest(request: Request): String {
        val (_, response, result) = request.responseString()

        when (result) {
            is Result.Failure -> {
                if (response.statusCode == 500) {
                    throw InvalidCredentialsException()
                }

                throw APIException("${request.method.value} request to ${request.url} failed (status code ${response.statusCode})")
            }
            is Result.Success -> {
                val body = result.get()

                // Uncomment this line to log a prettified version of the response
                println(gson.toJson(parser.parse(body).asJsonObject))

                val json = parser.parse(body).asJsonObject
                if (json.has("success") && !json["success"].bool) {
                    if (json.has("errors") && json["errors"].array.size() > 0) {
                        val error = json["errors"][0].string

                        if (error.startsWith("Hash doesn't match.")) {
                            throw InvalidCredentialsException()
                        }

                        throw APIException(error)
                    }

                    if (json.has("messages") && json["messages"].array.size() > 0) {
                        throw APIException(json["messages"][0].string)
                    }

                    throw APIException("Something went wrong (status code ${response.statusCode})")
                }

                return body
            }
        }
    }
}
