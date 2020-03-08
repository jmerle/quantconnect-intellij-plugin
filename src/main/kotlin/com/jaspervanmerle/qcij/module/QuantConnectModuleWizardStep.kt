package com.jaspervanmerle.qcij.module

import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.jaspervanmerle.qcij.ui.SettingsPanel
import javax.swing.JComponent

class QuantConnectModuleWizardStep(private val builder: QuantConnectModuleBuilder) : ModuleWizardStep() {
    private val settingsPanel = SettingsPanel()

    override fun getComponent(): JComponent {
        return settingsPanel.createComponent()
    }

    override fun updateDataModel() {
        builder.userId = settingsPanel.userId
        builder.apiToken = settingsPanel.apiToken
    }

    override fun validate(): Boolean {
        return settingsPanel.validate()
    }
}
