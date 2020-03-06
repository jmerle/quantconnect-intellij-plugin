package com.jaspervanmerle.qcij

import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent

class FileListener : BulkFileListener {
    override fun after(events: MutableList<out VFileEvent>) {
        // TODO(jmerle): Implement
    }
}
