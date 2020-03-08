package com.jaspervanmerle.qcij.api

import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.result.Result
import com.jaspervanmerle.qcij.api.converter.DateConverter
import com.jaspervanmerle.qcij.api.converter.JSONObjectConverter
import com.jaspervanmerle.qcij.api.model.APIException
import com.jaspervanmerle.qcij.api.model.InvalidCredentialsException
import com.jaspervanmerle.qcij.api.model.QuantConnectCredentials
import org.apache.commons.codec.digest.DigestUtils
import org.json.JSONObject

class APIClient(var credentials: QuantConnectCredentials? = null) {
    companion object {
        const val BASE_URL = "https://www.quantconnect.com/api/v2"
    }

    val klaxon = Klaxon()
        .converter(DateConverter())
        .converter(JSONObjectConverter())

    fun get(endpoint: String): String {
        return executeRequest(request(Method.GET, endpoint))
    }

    fun post(endpoint: String, body: JSONObject = JSONObject()): String {
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

                // TODO(jmerle): Remove debugging code
                println(JSONObject(body).toString(4))

                val json = JSONObject(body)
                if (json.has("success") && !json.getBoolean("success")) {
                    if (json.has("errors")) {
                        val errors = json.getJSONArray("errors")
                        if (errors.length() > 0) {
                            val error = errors.getString(0)

                            if (error.startsWith("Hash doesn't match.")) {
                                throw InvalidCredentialsException()
                            }

                            throw APIException(error)
                        }
                    }

                    if (json.has("messages")) {
                        val messages = json.getJSONArray("messages")
                        throw APIException(messages.getString(0))
                    }

                    throw APIException("Something went wrong (status code ${response.statusCode})")
                }

                return body
            }
        }
    }
}
