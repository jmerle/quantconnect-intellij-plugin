package com.jaspervanmerle.qcij.config

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

    init {
        // An arbitrary id is assigned to the project so that other classes can use a project-specific id when needed
        state.projectId = "${project.name}-${UUID.randomUUID()}"
    }

    override fun getState(): State? {
        return state
    }

    override fun loadState(newState: State) {
        state.projectId = newState.projectId
    }
}
