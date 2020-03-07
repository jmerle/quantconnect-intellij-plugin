package com.jaspervanmerle.qcij.module

import com.intellij.ide.BrowserUtil
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.openapi.options.ConfigurationException
import com.intellij.ui.layout.panel
import com.jaspervanmerle.qcij.ui.createPasswordField
import com.jaspervanmerle.qcij.ui.createTextField
import javax.swing.JComponent

class QuantConnectModuleWizardStep(private val builder: QuantConnectModuleBuilder) : ModuleWizardStep() {
    var userId = ""
    var apiToken = ""

    override fun getComponent(): JComponent {
        return panel {
            row {
                link("Retrieve your user ID and API token by going to quantconnect.com/account and clicking on the 'Request Email with Token' button.") {
                    BrowserUtil.browse("https://www.quantconnect.com/account")
                }
            }

            row("User ID:") {
                createTextField(::userId)()
            }

            row("API token:") {
                createPasswordField(::apiToken)()
            }
        }
    }

    override fun updateDataModel() {
        builder.userId = userId
        builder.apiToken = apiToken
    }

    override fun validate(): Boolean {
        if (userId.isBlank()) {
            throw ConfigurationException("User ID cannot be blank")
        }

        if (apiToken.isBlank()) {
            throw ConfigurationException("API token cannot be blank")
        }

        // TODO(jmerle): Check if User ID <> API token combination is valid

        return true
    }
}