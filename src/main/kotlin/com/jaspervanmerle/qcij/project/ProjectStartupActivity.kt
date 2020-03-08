package com.jaspervanmerle.qcij.project

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.jaspervanmerle.qcij.action.SyncAction
import com.jaspervanmerle.qcij.action.triggerAction

class ProjectStartupActivity : StartupActivity {
    override fun runActivity(project: Project) {
        if (!project.isQuantConnectProject()) {
            return
        }

        triggerAction<SyncAction>()
    }
}
