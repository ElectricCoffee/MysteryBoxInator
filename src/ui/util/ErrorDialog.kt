package ui.util

import java.awt.Component
import javax.swing.JOptionPane

class ErrorDialog(private val parent: Component) {
    fun open(iae: IllegalArgumentException) {
        JOptionPane.showMessageDialog(parent, iae.message, "Error", JOptionPane.ERROR_MESSAGE)
    }
}