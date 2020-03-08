package com.jaspervanmerle.qcij.project

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jaspervanmerle.qcij.config.CredentialsService

fun Project?.isQuantConnectProject() = this != null && service<CredentialsService>().getCredentials() != null
