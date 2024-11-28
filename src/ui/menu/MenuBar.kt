package ui.menu

import catalogue.Catalogue
import config.Config
import javax.swing.JMenuBar
import javax.swing.table.DefaultTableModel

class MenuBar(config: Config, catalogue: Catalogue, dtm: DefaultTableModel) : JMenuBar() {
    init {
        this.add(FileMenu(config, catalogue, dtm))
        this.add(HelpMenu(config, catalogue, dtm))
    }
}