package de.griefed

import Bulker
import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater {
        val bulker = Bulker()
        bulker.isVisible = true
    }
}