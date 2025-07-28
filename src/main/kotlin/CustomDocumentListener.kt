package de.griefed

import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class CustomDocumentListener(private val bulker: BulkerKt) : DocumentListener {
    override fun insertUpdate(e: DocumentEvent?) {
        bulker.refreshOutputFileList()
    }

    override fun removeUpdate(e: DocumentEvent?) {
        bulker.refreshOutputFileList()
    }

    override fun changedUpdate(e: DocumentEvent?) {
        bulker.refreshOutputFileList()
    }
}
