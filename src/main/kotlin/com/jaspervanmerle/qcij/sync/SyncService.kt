package com.jaspervanmerle.qcij.sync

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.newvfs.events.VFileEvent

class SyncService(private val project: Project) {
    fun processEvent(e: VFileEvent) {
        println("processEvent: ${e.path}")
    }
}
