package com.jaspervanmerle.qcij.sync

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.PerformInBackgroundOption
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.modifyModules
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.util.io.write
import com.jaspervanmerle.qcij.Notifications
import com.jaspervanmerle.qcij.api.APIService
import com.jaspervanmerle.qcij.api.model.QuantConnectProject
import com.jaspervanmerle.qcij.config.ConfigService
import java.io.File
import java.io.IOException
import java.nio.file.Paths
import java.time.Instant

class SyncTask(project: Project) : Task.Backgroundable(
    project,
    "Synchronizing QuantConnect projects and libraries",
    true,
    PerformInBackgroundOption.ALWAYS_BACKGROUND
) {
    private val api = project.service<APIService>()
    private val config = project.service<ConfigService>()

    private val baseDirectory = project.basePath!!

    private val sourceFoldersToCreate = mutableListOf<File>()

    override fun run(indicator: ProgressIndicator) {
        indicator.isIndeterminate = false
        indicator.fraction = 0.0

        indicator.text = "Pushing changes"
        push(indicator)

        indicator.text = "Pulling changes"
        pull(indicator)

        if (sourceFoldersToCreate.isNotEmpty()) {
            createSourceFolders()
        } else {
            LocalFileSystem.getInstance().refresh(true)
        }
    }

    private fun push(indicator: ProgressIndicator) {
        // TODO(jmerle): Push locally changed files

        indicator.fraction += 0.5
    }

    private fun pull(indicator: ProgressIndicator) {
        val projects = api.projects.getAll()
        for (qcProject in projects) {
            indicator.checkCanceled()

            indicator.fraction += 0.5 / projects.size.toDouble()

            if (qcProject.projectId !in config.syncedProjects) {
                createProject(qcProject)
                continue
            }

            if (qcProject.modified.isAfter(Instant.ofEpochSecond(config.syncedProjects[qcProject.projectId]!!))) {
                updateProject(qcProject)
                continue
            }
        }

        for ((projectId, _) in config.syncedProjects) {
            val deleted = projects.none { it.projectId == projectId }
            if (deleted) {
                deleteProject(projectId)
            }
        }
    }

    private fun createProject(qcProject: QuantConnectProject) {
        config.projectRoots[qcProject.projectId] = qcProject.name

        var successful = true

        for (qcFile in api.files.getAll(qcProject.projectId)) {
            val filePath = qcProject.getFilePath(qcFile)

            try {
                Paths.get(baseDirectory, filePath).write(qcFile.content)
                config.syncedFiles[filePath] = Instant.now().epochSecond
            } catch (ex: IOException) {
                Notifications.error("Could not create file '$filePath'")
                successful = false
            }
        }

        val projectDirectory = Paths.get(baseDirectory, qcProject.name).toFile()
        if (projectDirectory.exists()) {
            sourceFoldersToCreate += projectDirectory
        }

        if (successful) {
            config.syncedProjects[qcProject.projectId] = Instant.now().epochSecond
        } else {
            Notifications.error("Could not (completely) pull project '${qcProject.name}'")
        }
    }

    private fun updateProject(qcProject: QuantConnectProject) {
        for (qcFile in api.files.getAll(qcProject.projectId)) {
            val filePath = qcProject.getFilePath(qcFile)
            if (qcFile.modified.isAfter(Instant.ofEpochSecond(config.syncedFiles[filePath]!!))) {
                // TODO(jmerle): Do this in an IntelliJ-nice way, maybe inside a WriteAction?
                Paths.get(baseDirectory, filePath).toFile().writeText(qcFile.content)
                config.syncedFiles[filePath] = Instant.now().epochSecond
            }
        }
    }

    private fun deleteProject(projectId: Int) {
        val projectRoot = config.projectRoots[projectId]!!

        if (!Paths.get(baseDirectory, projectRoot).toFile().deleteRecursively()) {
            Notifications.error("Could not delete '$projectRoot'")
            return
        }

        config.syncedProjects.remove(projectId)
        config.projectRoots.remove(projectId)

        val filesToRemove = config.syncedFiles.keys.filter { it.startsWith(projectRoot) }
        for (file in filesToRemove) {
            config.syncedFiles.remove(file)
        }
    }

    private fun createSourceFolders() {
        ApplicationManager.getApplication().invokeLater {
            project.modifyModules {
                WriteAction.run<Throwable> {
                    val fs = LocalFileSystem.getInstance()
                    fs.refresh(false)

                    val model = ModuleRootManager.getInstance(modules[0]).modifiableModel
                    val contentEntry = model.contentEntries[0]

                    for (directory in sourceFoldersToCreate) {
                        contentEntry.addSourceFolder(fs.findFileByIoFile(directory)!!, false)
                    }

                    model.commit()
                }
            }
        }
    }
}
