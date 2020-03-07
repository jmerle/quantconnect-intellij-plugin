package com.jaspervanmerle.qcij.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jaspervanmerle.qcij.Notifications
import com.jaspervanmerle.qcij.config.CredentialsService

abstract class ProjectAction : AnAction() {
    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = e.project != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        if (e.project == null) return
        val project = e.project!!

        val credentials = project.service<CredentialsService>().getCredentials()
        if (credentials == null) {
            Notifications.error("This is not a QuantConnect project, create one in the New Project Wizard.", project)
            return
        }

        execute(project)
    }

    protected abstract fun execute(project: Project)
}
