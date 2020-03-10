package com.jaspervanmerle.qcij.project

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.jaspervanmerle.qcij.action.SyncAction
import com.jaspervanmerle.qcij.action.triggerAction
import com.jaspervanmerle.qcij.sync.SyncService

class ProjectStartupActivity : StartupActivity {
    override fun runActivity(project: Project) {
        if (!project.isQuantConnectProject()) {
            return
        }

        val projectManager = ProjectRootManager.getInstance(project)
        val syncService = project.service<SyncService>()

        project.messageBus.connect().subscribe(VirtualFileManager.VFS_CHANGES, object : BulkFileListener {
            override fun after(events: MutableList<out VFileEvent>) {
                val projectEvents = events.filter { it.file != null && projectManager.fileIndex.isInContent(it.file!!) }
                if (projectEvents.isNotEmpty()) {
                    syncService.processEvents(projectEvents)
                }
            }
        })

        triggerAction<SyncAction>()
    }
}
