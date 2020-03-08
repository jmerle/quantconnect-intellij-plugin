package com.jaspervanmerle.qcij.action

import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.AnActionEvent

fun triggerAction(actionId: String) {
    val actionManager = ActionManager.getInstance()
    val action = actionManager.getAction(actionId)

    DataManager.getInstance().dataContextFromFocusAsync
        .onSuccess {
            val actionEvent = AnActionEvent.createFromDataContext(ActionPlaces.UNKNOWN, null, it)
            action.actionPerformed(actionEvent)
        }
}

inline fun <reified T> triggerAction() = triggerAction(T::class.java.name)
