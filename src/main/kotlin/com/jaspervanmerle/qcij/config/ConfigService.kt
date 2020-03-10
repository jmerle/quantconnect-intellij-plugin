package com.jaspervanmerle.qcij.config

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.project.Project
import java.util.UUID

@State(name = "QuantConnectConfig")
class ConfigService(project: Project) : PersistentStateComponent<ConfigService.State> {
    class State {
        // An arbitrary project-specific id which is persistent even when the project is renamed
        var projectId = ""

        // QuantConnect project id -> timestamp (Epoch seconds) of when a file in it was last modified locally
        var syncedProjects = mutableMapOf<Int, Long>()

        // File path relative to project root -> timestamp (Epoch seconds) it was last modified
        var syncedFiles = mutableMapOf<String, Long>()

        // QuantConnect project id -> root path relative to the project
        var projectRoots = mutableMapOf<Int, String>()
    }

    private var state = State()

    val projectId: String
        get() = state.projectId

    val syncedProjects: MutableMap<Int, Long>
        get() = state.syncedProjects

    val syncedFiles: MutableMap<String, Long>
        get() = state.syncedFiles

    val projectRoots: MutableMap<Int, String>
        get() = state.projectRoots

    init {
        // An arbitrary id is assigned to the project so that other classes can use a project-specific id when needed
        state.projectId = "${project.name}-${UUID.randomUUID()}"
    }

    override fun getState(): State? {
        return state
    }

    override fun loadState(newState: State) {
        state.projectId = newState.projectId
        state.syncedProjects = newState.syncedProjects
        state.syncedFiles = newState.syncedFiles
        state.projectRoots = newState.projectRoots
    }
}
