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
import java.nio.file.Path
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

    override fun run(indicator: ProgressIndicator) {
        indicator.isIndeterminate = false
        indicator.fraction = 0.0

        indicator.text = "Pushing changes"
        push(indicator)

        indicator.text = "Pulling changes"
        pull(indicator)
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

            val localModified = Instant.ofEpochSecond(config.syncedProjects[qcProject.projectId]!!)
            if (qcProject.modified.isAfter(localModified)) {
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
        createSourceFolder(qcProject.name.toFile())

        config.projectRoots[qcProject.projectId] = qcProject.name

        for (qcFile in api.files.getAll(qcProject.projectId)) {
            writeFileIfNecessary(qcProject, qcFile)
        }

        config.syncedProjects[qcProject.projectId] = Instant.now().epochSecond
    }

    private fun updateProject(qcProject: QuantConnectProject) {
        val qcFiles = api.files.getAll(qcProject.projectId)

        val filesToDelete = getFilesInProject(qcProject.name, true).toMutableList()

        for (qcFile in qcFiles) {
            filesToDelete.removeIf { qcFile.name.startsWith(it) }
            writeFileIfNecessary(qcProject, qcFile)
        }

        for (fileToDelete in filesToDelete) {
            val localPath = Paths.get(qcProject.name, fileToDelete).toString()
            localPath.toFile().deleteRecursively()
            config.syncedFiles.remove(localPath)
        }
    }

    private fun deleteProject(projectId: Int) {
        val projectRoot = config.projectRoots[projectId]!!

        for (file in getFilesInProject(projectRoot, false)) {
            config.syncedFiles.remove(Paths.get(projectRoot, file).toString())
        }

        config.syncedProjects.remove(projectId)
        config.projectRoots.remove(projectId)

        projectRoot.toFile().deleteRecursively()
    }

    private fun writeFileIfNecessary(qcProject: QuantConnectProject, qcFile: QuantConnectFile) {
        val localPath = Paths.get(qcProject.name, qcFile.name).toString()

        if (localPath in config.syncedFiles) {
            val localModified = Instant.ofEpochSecond(config.syncedFiles[localPath]!!)
            if (!qcFile.modified.isAfter(localModified)) {
                return
            }
        }

        localPath.toPath().write(qcFile.content)
        config.syncedFiles[localPath] = Instant.now().epochSecond
    }

    private fun getFilesInProject(projectRoot: String, includeDirectories: Boolean): List<String> {
        val projectDirectory = projectRoot.toFile()
        return projectDirectory
            .walkTopDown()
            .filter { includeDirectories || it.isFile }
            .map { it.relativeTo(projectDirectory) }
            .map { it.path.replace("\\", "/") }
            .filter { it.isNotBlank() }
            .toList()
    }

    private fun createSourceFolder(directory: File) {
        ApplicationManager.getApplication().invokeLater {
            project.modifyModules {
                WriteAction.run<Throwable> {
                    val model = ModuleRootManager.getInstance(modules[0]).modifiableModel

                    val virtualDirectory = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(directory)!!
                    model.contentEntries[0].addSourceFolder(virtualDirectory, false)

                    model.commit()
                }
            }
        }
    }

    private fun String.toPath(): Path {
        return Paths.get(project.basePath!!, this)
    }

    private fun String.toFile(): File {
        return toPath().toFile()
    }
}
