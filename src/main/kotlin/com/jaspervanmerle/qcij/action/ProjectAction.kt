package com.jaspervanmerle.qcij.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.jaspervanmerle.qcij.project.isQuantConnectProject
import com.jaspervanmerle.qcij.ui.Notifications

abstract class ProjectAction : AnAction() {
    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = e.project != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project

        if (!project.isQuantConnectProject()) {
            Notifications.error(project, "This is not a QuantConnect project, create one in the New Project Wizard.")
            return
        }

        execute(project!!)
    }

    protected abstract fun execute(project: Project)
}
