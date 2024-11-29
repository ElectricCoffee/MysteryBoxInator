package ui.util

import java.awt.Component
import javax.swing.JOptionPane

class ErrorDialog(private val parent: Component) {
    fun open(iae: IllegalArgumentException, title: String = "Error") {
        JOptionPane.showMessageDialog(parent, iae.message, title, JOptionPane.ERROR_MESSAGE)
    }

    fun open(message: String, title: String) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE)
    }
}