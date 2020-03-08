package com.jaspervanmerle.qcij.api.model

import java.util.Date

data class QuantConnectFile(
    val name: String,
    val content: String,
    val modified: Date,
    val projectId: Int,
    val open: Int,
    val userHasAccess: Boolean,
    val readOnly: Boolean,
    val binary: Boolean?
)
