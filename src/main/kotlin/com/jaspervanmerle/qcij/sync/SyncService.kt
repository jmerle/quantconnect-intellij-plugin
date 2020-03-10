package com.jaspervanmerle.qcij.sync

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.newvfs.events.VFileEvent

class SyncService(private val project: Project) {
    fun processEvents(events: List<VFileEvent>) {
        println("Events: ${events.size}")
    }
}
