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
import com.intellij.util.FileContentUtil
import com.intellij.util.io.createDirectories
import com.intellij.util.io.write
import com.jaspervanmerle.qcij.api.APIService
import com.jaspervanmerle.qcij.api.model.QuantConnectFile
import com.jaspervanmerle.qcij.api.model.QuantConnectProject
import com.jaspervanmerle.qcij.config.ConfigService
import java.io.File
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

    private val sourceFoldersToCreate = mutableListOf<File>()

    override fun run(indicator: ProgressIndicator) {
        indicator.isIndeterminate = false
        indicator.fraction = 0.0

        indicator.text = "Pushing changes"
        push(indicator)

        indicator.text = "Pulling changes"
        pull(indicator)

        createSourceFolders()
    }

    override fun onSuccess() {
        LocalFileSystem.getInstance().refresh(true)
        FileContentUtil.reparseOpenedFiles()
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

            if (qcProject.modified.isAfter(config.syncedProjects[qcProject.projectId]!!)) {
                updateProject(qcProject)
                continue
            }
        }

        for ((projectId, _) in config.syncedProjects) {
            if (projects.none { it.projectId == projectId }) {
                deleteProject(projectId)
            }
        }
    }

    private fun createProject(qcProject: QuantConnectProject) {
        qcProject.name.toPath().createDirectories()

        config.projectRoots[qcProject.projectId] = qcProject.name
        sourceFoldersToCreate += qcProject.name.toFile()

        for (qcFile in api.files.getAll(qcProject.projectId)) {
            val filePath = getFilePath(qcProject, qcFile)
            filePath.toPath().write(qcFile.content)
            config.syncedFiles[filePath] = Instant.now().epochSecond
        }

        config.syncedProjects[qcProject.projectId] = Instant.now().epochSecond
    }

    private fun updateProject(qcProject: QuantConnectProject) {
        val qcFiles = api.files.getAll(qcProject.projectId)

        val projectDirectory = qcProject.name.toFile()
        val filesToDelete = qcProject.name
            .toFile()
            .walkTopDown()
            .map { it.relativeTo(projectDirectory) }
            .map { it.path.replace("\\", "/") }
            .filter { it.isNotBlank() }
            .toMutableList()

        for (qcFile in qcFiles) {
            val filePath = getFilePath(qcProject, qcFile)

            filesToDelete.removeIf { qcFile.name.startsWith(it) }

            if (filePath !in config.syncedFiles ||
                qcFile.modified.isAfter(config.syncedFiles[filePath]!!)) {
                filePath.toPath().write(qcFile.content)
                config.syncedFiles[filePath] = Instant.now().epochSecond
            }
        }

        for (fileToDelete in filesToDelete) {
            val file = getFilePath(qcProject, fileToDelete).toFile()
            if (file.exists()) {
                file.deleteRecursively()
            }

            config.syncedFiles.remove(fileToDelete)
        }
    }

    private fun deleteProject(projectId: Int) {
        val projectRoot = config.projectRoots[projectId]!!

        projectRoot.toFile().deleteRecursively()

        config.syncedProjects.remove(projectId)
        config.projectRoots.remove(projectId)

        val filesToRemove = config.syncedFiles.keys.filter { it.startsWith(projectRoot) }
        for (file in filesToRemove) {
            config.syncedFiles.remove(file)
        }
    }

    private fun createSourceFolders() {
        if (sourceFoldersToCreate.isEmpty()) {
            return
        }

        ApplicationManager.getApplication().invokeLater {
            project.modifyModules {
                val fs = LocalFileSystem.getInstance()

                WriteAction.runAndWait<Throwable> {
                    fs.refresh(false)
                }

                val model = ModuleRootManager.getInstance(modules[0]).modifiableModel
                val contentEntry = model.contentEntries[0]

                for (directory in sourceFoldersToCreate) {
                    contentEntry.addSourceFolder(fs.findFileByIoFile(directory)!!, false)
                }

                WriteAction.run<Throwable> {
                    model.commit()
                }
            }
        }
    }

    private fun getFilePath(qcProject: QuantConnectProject, qcFile: QuantConnectFile): String {
        return Paths.get(qcProject.name, qcFile.name).toString()
    }

    private fun getFilePath(qcProject: QuantConnectProject, qcFileName: String): String {
        return Paths.get(qcProject.name, qcFileName).toString()
    }

    private fun String.toPath() = Paths.get(project.basePath!!, this)
    private fun String.toFile() = toPath().toFile()
    private fun Instant.isAfter(timestamp: Long) = isAfter(Instant.ofEpochSecond(timestamp))
}
