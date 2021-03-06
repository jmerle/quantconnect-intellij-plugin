package com.jaspervanmerle.qcij.config

import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.Credentials
import com.intellij.credentialStore.generateServiceName
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jaspervanmerle.qcij.api.model.QuantConnectCredentials

class CredentialsService(private val project: Project) {
    fun getCredentials(): QuantConnectCredentials? {
        val credentials = PasswordSafe.instance.get(createCredentialAttributes()) ?: return null

        val userId = credentials.userName ?: return null
        val apiToken = credentials.getPasswordAsString() ?: return null

        return QuantConnectCredentials(userId, apiToken)
    }

    fun setCredentials(userId: String?, apiToken: String?) {
        PasswordSafe.instance.set(createCredentialAttributes(), Credentials(userId, apiToken))

        val publisher = project.messageBus.syncPublisher(CredentialsListener.TOPIC)
        publisher.onCredentialsChange(getCredentials())
    }

    private fun createCredentialAttributes(): CredentialAttributes {
        val subsystem = "com.jaspervanmerle.qcij.config.CredentialsService"
        val key = project.service<ConfigService>().projectId

        return CredentialAttributes(generateServiceName(subsystem, key))
    }
}
