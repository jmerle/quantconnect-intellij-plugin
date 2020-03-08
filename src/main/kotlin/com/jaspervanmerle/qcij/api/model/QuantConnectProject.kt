package com.jaspervanmerle.qcij.api.model

// Taken from https://github.com/trestrantham/quantconnect-filesync/blob/master/src/types.ts

data class QuantConnectProject(
    val projectId: Int,
    val name: String,
    val modified: String,
    val created: String,
    val ownerId: Int,
    val language: String,
    val collaborators: List<QuantConnectCollaborator>,
    val leanVersionId: Int,
    val owner: Boolean,
    val description: String,
    val channelId: String,
    val files: List<QuantConnectFile>
)
