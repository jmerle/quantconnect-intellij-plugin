package com.jaspervanmerle.qcij.action

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jaspervanmerle.qcij.api.APIService

class SyncAction : ProjectAction() {
    override fun execute(project: Project) {
        val api = project.service<APIService>()

        val compile = api.compiles.create(3910361)
        val status = api.compiles.get(3910361, compile.compileId)
        println(status)

        // TODO(jmerle): Implement
    }
}
