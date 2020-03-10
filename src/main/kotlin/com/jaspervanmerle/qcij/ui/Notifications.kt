package com.jaspervanmerle.qcij.ui

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object Notifications {
    private val notificationGroup = NotificationGroup("QuantConnect", NotificationDisplayType.BALLOON, true)

    private fun notify(project: Project?, content: String, type: NotificationType) {
        val notification = notificationGroup.createNotification("QuantConnect", content, type, null)
        notification.notify(project)
    }

    fun info(project: Project?, content: String) = notify(project, content, NotificationType.INFORMATION)
    fun warn(project: Project?, content: String) = notify(project, content, NotificationType.WARNING)
    fun error(project: Project?, content: String) = notify(project, content, NotificationType.ERROR)
}
