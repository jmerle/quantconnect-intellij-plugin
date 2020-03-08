package com.jaspervanmerle.qcij.api.model

data class QuantConnectCompileFile(
    val file: String,
    val parameters: List<QuantConnectCompileParameter>
)
