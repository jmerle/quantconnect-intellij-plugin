package com.jaspervanmerle.qcij.module

import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.service
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.roots.ModifiableRootModel
import com.jaspervanmerle.qcij.config.CredentialsService

class QuantConnectModuleBuilder : ModuleBuilder() {
    var userId = ""
    var apiToken = ""

    override fun getModuleType(): ModuleType<*> {
        return QuantConnectModuleType.getInstance()
    }

    override fun setupRootModel(modifiableRootModel: ModifiableRootModel) {
        val credentialsService = modifiableRootModel.project.service<CredentialsService>()
        credentialsService.setCredentials(userId, apiToken)

        doAddContentEntry(modifiableRootModel)
    }

    override fun getCustomOptionsStep(context: WizardContext?, parentDisposable: Disposable?): ModuleWizardStep? {
        return QuantConnectModuleWizardStep(this)
    }
}
