package com.jaspervanmerle.qcij.api.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class QuantConnectCompileState(private val value: String) {
    IN_QUEUE("InQueue"),
    BUILD_SUCCESS("BuildSuccess"),
    BUILD_ERROR("BuildError");

    companion object {
        private val allValues = QuantConnectCompileState.values()

        @JsonCreator
        fun forValue(value: String): QuantConnectCompileState? {
            return allValues.firstOrNull { it.value == value }
        }
    }

    @JsonValue
    fun toValue(): String {
        return value
    }
}
