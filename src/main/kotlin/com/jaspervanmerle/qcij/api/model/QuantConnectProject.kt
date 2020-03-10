package com.jaspervanmerle.qcij.api.model

import java.nio.file.Paths
import java.time.Instant

data class QuantConnectProject(
    val projectId: Int,
    val name: String,
    val modified: Instant,
    val created: Instant,
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
) {
    fun getFilePath(file: QuantConnectFile): String {
        return Paths.get(name, file.name).toString()
    }
}
