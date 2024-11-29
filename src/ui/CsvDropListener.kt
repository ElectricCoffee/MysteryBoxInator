package ui

import catalogue.Catalogue
import catalogue.CsvLoadMode
import config.Config
import ui.util.TableUtils
import java.awt.datatransfer.DataFlavor
import java.awt.dnd.*
import java.io.File
import java.util.*
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.table.DefaultTableModel
import kotlin.io.path.Path

class CsvDropListener(private val parent: JFrame, private val config: Config, private val catalogue: Catalogue, private val dtm: DefaultTableModel) : DropTargetListener {
    override fun dragEnter(dtde: DropTargetDragEvent?) {}

    override fun dragOver(dtde: DropTargetDragEvent?) {}

    override fun dropActionChanged(dtde: DropTargetDragEvent?) {}

    override fun dragExit(dte: DropTargetEvent?) {}

    override fun drop(dtde: DropTargetDropEvent?) {
        dtde?.acceptDrop(DnDConstants.ACTION_COPY)

        try {
            // Retrieve the dropped data
            val transferable = dtde!!.transferable
            if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                // Get the list of files dropped
                val droppedFiles = transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>

                for (file in droppedFiles) {
                    // Only process PDF files
                    if (file.name.lowercase(Locale.getDefault()).endsWith(".csv")) {
                        // Get the file path and use it
                        val filePath = file.absolutePath
                        val loadMode = when(openDialog(filePath)) {
                            0 -> CsvLoadMode.APPEND
                            1 -> CsvLoadMode.OVERWRITE
                            else -> break
                        }
                        catalogue.appendFromFile(Path(filePath), loadMode, startIndex = 1)
                        TableUtils.populateTable(config, catalogue, dtm)
                    } else {
                        JOptionPane.showMessageDialog(
                            null,
                            "Only PDF files are accepted.",
                            "Incorrect file type",
                            JOptionPane.INFORMATION_MESSAGE
                        )
                    }
                }
            }
        } catch (ex: Exception) {
            JOptionPane.showMessageDialog(
                null,
                ex.message,
                ex.javaClass.canonicalName,
                JOptionPane.ERROR_MESSAGE
            )
        } finally {
            dtde!!.dropComplete(true)
        }
    }

    private fun openDialog(filePath: String): Int {
        val options = arrayOf("Append", "Overwrite", "Cancel")
        return JOptionPane.showOptionDialog(
            parent,
            "Loading $filePath, do you wish to append or overwrite?",
            "What should I do?",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[2]
        )
    }
}