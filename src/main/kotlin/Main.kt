package de.griefed

import Bulker
import javax.swing.SwingUtilities

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    // Launch the book editor form
    SwingUtilities.invokeLater {
        val bulker = Bulker()
        bulker.isVisible = true
    }
}