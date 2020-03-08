package com.jaspervanmerle.qcij.config

import com.intellij.util.messages.Topic
import com.jaspervanmerle.qcij.api.model.QuantConnectCredentials

interface CredentialsListener {
    companion object {
        val TOPIC = Topic(CredentialsListener::class.java)
    }

    fun onCredentialsChange(newCredentials: QuantConnectCredentials?)
}
