package com.jaspervanmerle.qcij.config

import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.jaspervanmerle.qcij.api.model.QuantConnectCredentials
import com.jaspervanmerle.qcij.ui.SettingsPanel
import javax.swing.JComponent

class QuantConnectConfigurable(project: Project) : Configurable {
    private val credentialsService = project.service<CredentialsService>()

    private val settingsPanel = SettingsPanel()
    private var currentCredentials = QuantConnectCredentials("", "")

    init {
        val credentials = credentialsService.getCredentials()

        if (credentials != null) {
            settingsPanel.userId = credentials.userId
            settingsPanel.apiToken = credentials.apiToken

            currentCredentials = credentials
        }
    }

    override fun getDisplayName(): String {
        return "QuantConnect"
    }

    override fun createComponent(): JComponent? {
        return settingsPanel.createComponent()
    }

    override fun isModified(): Boolean {
        return settingsPanel.userId != currentCredentials.userId ||
            settingsPanel.apiToken != currentCredentials.apiToken
    }

    override fun apply() {
        settingsPanel.validate()

        credentialsService.setCredentials(settingsPanel.userId, settingsPanel.apiToken)
        currentCredentials = credentialsService.getCredentials()!!
    }
}
