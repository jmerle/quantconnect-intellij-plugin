package com.jaspervanmerle.qcij.api.model

import java.time.Instant

data class QuantConnectFile(
    val name: String,
    val content: String,
    val modified: Instant,
    val projectId: Int,
    val open: Int,
    val userHasAccess: Boolean,
    val readOnly: Boolean,
    val binary: Boolean?
)
