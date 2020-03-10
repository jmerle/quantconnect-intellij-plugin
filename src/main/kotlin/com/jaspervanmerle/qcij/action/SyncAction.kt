package com.jaspervanmerle.qcij.action

import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.jaspervanmerle.qcij.sync.SyncTask

class SyncAction : ProjectAction() {
    override fun execute(project: Project) {
        ProgressManager.getInstance().run(SyncTask(project))
    }
}
