package com.jaspervanmerle.qcij.module

import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.module.ModuleTypeManager
import com.jaspervanmerle.qcij.QuantConnectIcons
import javax.swing.Icon

class QuantConnectModuleType : ModuleType<QuantConnectModuleBuilder>(ID) {
    companion object {
        private const val ID = "com.jaspervanmerle.qcij.module.QuantConnectModuleType"

        fun getInstance(): QuantConnectModuleType {
            return ModuleTypeManager.getInstance().findByID(ID) as QuantConnectModuleType
        }
    }

    override fun createModuleBuilder(): QuantConnectModuleBuilder {
        return QuantConnectModuleBuilder()
    }

    override fun getName(): String {
        return "QuantConnect"
    }

    override fun getDescription(): String {
        return "QuantConnect project with synced projects and libraries"
    }

    override fun getNodeIcon(isOpened: Boolean): Icon {
        return QuantConnectIcons.LOGO
    }
}
