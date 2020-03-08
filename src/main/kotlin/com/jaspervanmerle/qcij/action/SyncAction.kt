package com.jaspervanmerle.qcij.action

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jaspervanmerle.qcij.api.APIService

class SyncAction : ProjectAction() {
    override fun execute(project: Project) {
        val api = project.service<APIService>()

        api.files.getAll(3910361)

        // TODO(jmerle): Implement
    }
}
