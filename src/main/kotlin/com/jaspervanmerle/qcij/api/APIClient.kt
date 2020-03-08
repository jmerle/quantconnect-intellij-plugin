package com.jaspervanmerle.qcij.api

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.result.Result
import com.jaspervanmerle.qcij.api.model.APIException
import com.jaspervanmerle.qcij.api.model.InvalidCredentialsException
import com.jaspervanmerle.qcij.api.model.QuantConnectCredentials
import java.security.MessageDigest
import java.text.SimpleDateFormat
import org.json.JSONObject

class APIClient(var credentials: QuantConnectCredentials? = null) {
    companion object {
        const val BASE_URL = "https://www.quantconnect.com/api/v2"
    }

    val objectMapper: ObjectMapper = ObjectMapper()
        .registerModule(KotlinModule())
        .registerModule(JsonOrgModule())
        .setDateFormat(SimpleDateFormat("yyyy-LL-dd HH:mm:ss"))
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun get(endpoint: String): String {
        return executeRequest(request(Method.GET, endpoint))
    }

    fun post(endpoint: String, body: Map<String, Any> = emptyMap()): String {
        return executeRequest(request(Method.POST, endpoint).body(JSONObject(body).toString()))
    }

    private fun request(method: Method, endpoint: String): Request {
        val request = Fuel.request(method, BASE_URL + endpoint)

        if (credentials == null) {
            return request
        }

        val userId = credentials!!.userId
        val apiToken = credentials!!.apiToken

        val timestamp = System.currentTimeMillis() / 1000
        val hash = MessageDigest
            .getInstance("SHA-256")
            .digest("$apiToken:$timestamp".toByteArray())
            .fold("", { str, it -> str + "%02x".format(it) })

        return request
            .authentication()
            .basic(userId, hash)
            .header("Timestamp", timestamp)
    }

    private fun executeRequest(request: Request): String {
        val (_, response, result) = request.responseString()

        if (response.statusCode == 500) {
            throw InvalidCredentialsException()
        }

        if (result is Result.Failure || response.statusCode < 200 || response.statusCode >= 300) {
            throw APIException("${request.method} request to ${request.url} failed (status code ${response.statusCode})")
        }

        val body = result.get()
        val json = JSONObject(body)

        // TODO(jmerle): Remove debugging code
        println(json.toString(4))

        if (!json.getBoolean("success")) {
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
