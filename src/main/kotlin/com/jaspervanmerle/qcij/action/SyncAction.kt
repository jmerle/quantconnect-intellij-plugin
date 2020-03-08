package com.jaspervanmerle.qcij.action

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jaspervanmerle.qcij.api.APIService

class SyncAction : ProjectAction() {
    override fun execute(project: Project) {
        val api = project.service<APIService>()

        api.backtests.getAll(3910361)
        val bt = api.backtests.get(3910361, "d24bef4aef0b5204bcc4307d1053e7d7")
        println(bt.result)

        // TODO(jmerle): Implement
    }
}
