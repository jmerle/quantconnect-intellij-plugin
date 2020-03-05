package com.jaspervanmerle.qcij

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages

class HelloWorldAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val title = "QuantConnect for IntelliJ"
        val message = "Hello world!"

        Messages.showMessageDialog(e.project, message, title, Messages.getInformationIcon())
    }
}
