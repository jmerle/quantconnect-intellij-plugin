package com.jaspervanmerle.qcij.api.model

import java.util.Date

data class QuantConnectProject(
    val projectId: Int,
    val name: String,
    val modified: Date,
    val created: Date,
    val ownerId: Int,
    val language: String,
    val collaborators: List<QuantConnectCollaborator>,
    val leanVersionId: Int,
    val owner: Boolean,
    val description: String,
    val channelId: String,
    val parameters: List<QuantConnectParameter>,
    val libraries: List<Int>,
    val isAlphaStreamDeployment: Int
)
