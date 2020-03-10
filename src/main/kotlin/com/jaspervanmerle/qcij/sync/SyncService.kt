package com.jaspervanmerle.qcij.sync

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.newvfs.events.VFileEvent

class SyncService(private val project: Project) {
    fun processEvents(events: List<VFileEvent>) {
        for (event in events) {
            println("${event.path} | ${event.requestor} | ${event.isFromRefresh} | ${event.isFromSave} | ${event.isValid}")
        }
    }
}
