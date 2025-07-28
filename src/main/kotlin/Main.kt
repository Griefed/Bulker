package de.griefed

import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater {
        val bulker = BulkerKt()
        bulker.makeVisible()
    }
}