package ui.menu

import catalogue.Catalogue
import config.Config
import ui.menu.file.ClearCatalogueItem
import ui.menu.file.EditConfigItem
import ui.menu.file.LoadCatalogueCsv
import ui.menu.file.OpenOutputFolderItem
import javax.swing.JMenu
import javax.swing.table.DefaultTableModel

class FileMenu(config: Config, catalogue: Catalogue, dtm: DefaultTableModel) : JMenu("File") {
    init {
        getAccessibleContext().accessibleDescription = "This menu handles file operations"
        try {
            add(LoadCatalogueCsv(config, catalogue, dtm))
            add(EditConfigItem())
            add(OpenOutputFolderItem(config))
            add(ClearCatalogueItem())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}