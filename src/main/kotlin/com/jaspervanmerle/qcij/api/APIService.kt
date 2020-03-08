package com.jaspervanmerle.qcij.api

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jaspervanmerle.qcij.api.client.BacktestClient
import com.jaspervanmerle.qcij.api.client.CompileClient
import com.jaspervanmerle.qcij.api.client.FileClient
import com.jaspervanmerle.qcij.api.client.ProjectClient
import com.jaspervanmerle.qcij.api.model.QuantConnectCredentials
import com.jaspervanmerle.qcij.config.CredentialsListener
import com.jaspervanmerle.qcij.config.CredentialsService

class APIService(project: Project) {
    private val api = APIClient(project.service<CredentialsService>().getCredentials())

    init {
        project.messageBus.connect().subscribe(CredentialsListener.TOPIC, object : CredentialsListener {
            override fun onCredentialsChange(newCredentials: QuantConnectCredentials?) {
                api.credentials = newCredentials
            }
        })
    }

    val files = FileClient(api)
    val projects = ProjectClient(api)
    val backtests = BacktestClient(api)
    val compiles = CompileClient(api)
}
