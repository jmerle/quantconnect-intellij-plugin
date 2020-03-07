package com.jaspervanmerle.qcij.ui

import com.intellij.ui.components.JBCheckBox
import javax.swing.JComponent
import javax.swing.JPasswordField
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.JTextComponent
import kotlin.reflect.KMutableProperty0

// The default form components in the Kotlin UI DSL do not seem to call setters on change, these do

private fun wrapTextComponent(component: JTextComponent, property: KMutableProperty0<String>): JTextComponent {
    component.text = property.get()

    component.document.addDocumentListener(object : DocumentListener {
        override fun changedUpdate(e: DocumentEvent?) {
            property.set(component.text)
        }

        override fun insertUpdate(e: DocumentEvent?) {
            property.set(component.text)
        }

        override fun removeUpdate(e: DocumentEvent?) {
            property.set(component.text)
        }
    })

    return component
}

fun createTextField(property: KMutableProperty0<String>): JComponent = wrapTextComponent(JTextField(), property)
fun createPasswordField(property: KMutableProperty0<String>): JComponent = wrapTextComponent(JPasswordField(), property)

fun createCheckBox(property: KMutableProperty0<Boolean>, label: String? = null): JComponent {
    val component = JBCheckBox(label)
    component.isSelected = property.get()

    component.addItemListener {
        property.set(component.isSelected)
    }

    return component
}
