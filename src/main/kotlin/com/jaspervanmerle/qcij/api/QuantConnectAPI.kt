package com.jaspervanmerle.qcij.api

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jaspervanmerle.qcij.config.CredentialsService

class QuantConnectAPI(var credentials: QuantConnectCredentials) {
    constructor(project: Project) : this(project.service<CredentialsService>().getCredentials()!!)

    // TODO(jmerle): Implement
}
