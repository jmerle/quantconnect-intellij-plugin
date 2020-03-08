package com.jaspervanmerle.qcij.action

import com.intellij.openapi.project.Project
import com.jaspervanmerle.qcij.Notifications

class SyncAction : ProjectAction() {
    override fun execute(project: Project) {
        // TODO(jmerle): Implement

        Notifications.info("Sync project", project)
    }
}
