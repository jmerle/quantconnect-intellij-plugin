package com.jaspervanmerle.qcij.api.model

// Taken from https://github.com/trestrantham/quantconnect-filesync/blob/master/src/types.ts

data class QuantConnectFile(
    val name: String,
    val content: String,
    val modified: String,
    val projectId: Int,
    val open: Int,
    val userHasAccess: Boolean,
    val readOnly: Boolean
)
