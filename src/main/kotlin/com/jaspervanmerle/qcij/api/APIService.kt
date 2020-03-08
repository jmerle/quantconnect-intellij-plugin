package com.jaspervanmerle.qcij.api

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jaspervanmerle.qcij.api.client.BacktestsClient
import com.jaspervanmerle.qcij.api.client.FilesClient
import com.jaspervanmerle.qcij.api.client.ProjectsClient
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

    val files = FilesClient(api)
    val projects = ProjectsClient(api)
    val backtests = BacktestsClient(api)
}
