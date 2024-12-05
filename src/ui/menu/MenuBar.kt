package ui.menu

import catalogue.Catalogue
import config.Config
import mysteryBox.MysteryBoxList
import javax.swing.JMenuBar
import javax.swing.table.DefaultTableModel

class MenuBar(config: Config, catalogue: Catalogue, mysteryBoxList: MysteryBoxList, catalogueDtm: DefaultTableModel, mysteryBoxDtm: DefaultTableModel) : JMenuBar() {
    init {
        this.add(FileMenu(config, catalogue, mysteryBoxList, catalogueDtm, mysteryBoxDtm))
        this.add(HelpMenu(config, catalogue, catalogueDtm))
    }
}