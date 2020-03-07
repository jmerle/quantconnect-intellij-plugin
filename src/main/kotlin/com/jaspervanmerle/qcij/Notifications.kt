package com.jaspervanmerle.qcij

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object Notifications {
    private val notificationGroup = NotificationGroup("QuantConnect", NotificationDisplayType.BALLOON, true)

    private fun notify(content: String, project: Project?, type: NotificationType) {
        val notification = notificationGroup.createNotification("QuantConnect", content, type, null)
        notification.notify(project)
    }

    fun info(content: String, project: Project? = null) = notify(content, project, NotificationType.INFORMATION)
    fun warn(content: String, project: Project? = null) = notify(content, project, NotificationType.WARNING)
    fun error(content: String, project: Project? = null) = notify(content, project, NotificationType.ERROR)
}
