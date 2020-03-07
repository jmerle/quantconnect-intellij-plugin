package com.jaspervanmerle.qcij.config

import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.Credentials
import com.intellij.credentialStore.generateServiceName
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.project.Project
import java.util.UUID

@State(name = "QuantConnectConfig")
class ConfigService(project: Project) : PersistentStateComponent<ConfigService.State> {
    class State {
        var projectId = ""
    }

    private val state = State()

    val projectId: String
        get() = state.projectId

    var userId: String?
        get() = getCredentials()?.userName
        set(value) = setCredentials(value, apiToken)

    var apiToken: String?
        get() = getCredentials()?.getPasswordAsString()
        set(value) = setCredentials(apiToken, value)

    init {
        // An arbitrary id is assigned to the project so that other classes have a project-specific id rely on when needed.
        state.projectId = "${project.name}-${UUID.randomUUID()}"
    }

    override fun getState(): State? {
        return state
    }

    override fun loadState(newState: State) {
        state.projectId = newState.projectId
    }

    private fun getCredentials(): Credentials? {
        return PasswordSafe.instance.get(createCredentialAttributes())
    }

    private fun setCredentials(userId: String?, apiToken: String?) {
        PasswordSafe.instance.set(createCredentialAttributes(), Credentials(userId, apiToken))
    }

    private fun createCredentialAttributes(): CredentialAttributes {
        return CredentialAttributes(generateServiceName("com.jaspervanmerle.qcij.config.ConfigService", state.projectId))
    }
}
