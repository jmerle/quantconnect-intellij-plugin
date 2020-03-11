package com.jaspervanmerle.qcij.project

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.jaspervanmerle.qcij.action.SyncAction
import com.jaspervanmerle.qcij.action.triggerAction
import com.jaspervanmerle.qcij.sync.SyncService
import java.nio.file.Paths

class ProjectStartupActivity : StartupActivity {
    override fun runActivity(project: Project) {
        if (!project.isQuantConnectProject()) {
            return
        }

        val syncService = project.service<SyncService>()

        project.messageBus.connect().subscribe(VirtualFileManager.VFS_CHANGES, object : BulkFileListener {
            override fun after(events: MutableList<out VFileEvent>) {
                val projectEvents = events.filter { isPathInProject(project, it.path) }
                if (projectEvents.isNotEmpty()) {
                    syncService.processEvents(projectEvents)
                }
            }
        })

        triggerAction<SyncAction>()
    }

    private fun isPathInProject(project: Project, path: String): Boolean {
        return try {
            Paths.get(project.basePath!!).relativize(Paths.get(path))
            !path.contains(Paths.get(project.basePath!!, Project.DIRECTORY_STORE_FOLDER).toString())
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}
