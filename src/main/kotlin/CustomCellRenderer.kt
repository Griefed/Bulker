package de.griefed

import java.awt.Component
import java.util.*
import javax.swing.DefaultListCellRenderer
import javax.swing.JList
import javax.swing.ListCellRenderer

class CustomCellRenderer : ListCellRenderer<Any> {
    var listCellRenderer: DefaultListCellRenderer = DefaultListCellRenderer()

    override fun getListCellRendererComponent(
        list: JList<out Any?>?,
        value: Any?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        listCellRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
        listCellRenderer.setText(getValueString(value))
        return listCellRenderer
    }

    private fun getValueString(value: Any?): String {
        var returnString = "null"
        if (value != null) {
            if (value is Hashtable<*, *>) {
                val hashtable = value
                val name = hashtable["name"] as String?
                val path = hashtable["path"] as String?
                returnString = "$name ==> $path"
            } else {
                returnString = "X: $value"
            }
        }
        return returnString
    }
}