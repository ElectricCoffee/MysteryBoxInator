package ui.menu

import catalogue.Catalogue
import config.Config
import mysteryBox.MysteryBoxList
import ui.menu.file.*
import javax.swing.JMenu
import javax.swing.table.DefaultTableModel

class FileMenu(config: Config, catalogue: Catalogue, mysteryBoxList: MysteryBoxList, dtm: DefaultTableModel) : JMenu("File") {
    init {
        getAccessibleContext().accessibleDescription = "This menu handles file operations"
        try {
            add(LoadCatalogueCsv(config, catalogue, dtm))
            add(EditConfigItem())
            add(OpenOutputFolderItem(config))
            addSeparator()
            add(ClearCatalogueItem(config, catalogue, dtm))
            addSeparator()
            add(ExportMysteryBoxesItem(config, mysteryBoxList))
            add(ClearMysteryBoxesItem())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}