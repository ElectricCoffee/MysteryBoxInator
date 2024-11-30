package ui

import java.util.logging.Filter
import javax.swing.text.AttributeSet
import javax.swing.text.DocumentFilter

class DecimalDocumentFilter : DocumentFilter() {
    override fun insertString(fb: FilterBypass, offset: Int, string: String, attr: AttributeSet?) {
        val str = getWholeString(fb, offset, string)
        if (isValidDecimal(str)) {
            super.insertString(fb, offset, string, attr)
        }
    }

    override fun replace(fb: FilterBypass, offset: Int, length: Int, text: String, attrs: AttributeSet?) {
        val str = getWholeString(fb, offset, text, length)
        if (isValidDecimal(str)) {
            super.replace(fb, offset, length, text, attrs)
        }
    }

    override fun remove(fb: FilterBypass, offset: Int, length: Int) {
        super.remove(fb, offset, length)
    }

    // Checks if the text is a valid decimal number
    private fun isValidDecimal(text: String): Boolean {
        val result = text.isEmpty() || text.matches(Regex("^-?\\d*(?:\\.\\d*)?\$"))
        println("$text $result")
        return result
    }

    private fun getWholeString(fb: FilterBypass, offset: Int, string: String, length: Int = 0): String {
        val currentText = fb.document.getText(0, fb.document.length) // Get current text
        return currentText.substring(0, offset) + string + currentText.substring(offset + length)
    }
}