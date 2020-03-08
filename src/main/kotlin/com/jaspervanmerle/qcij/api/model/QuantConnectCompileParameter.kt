package com.jaspervanmerle.qcij.api.model

data class QuantConnectCompileParameter(
    val file: String,
    val parameters: List<QuantConnectCompileParameterItem>
)
