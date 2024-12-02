package ui.listeners

import catalogue.Catalogue
import config.Config
import mysteryBox.MysteryBox
import mysteryBox.MysteryBoxList
import mysteryBox.assembler.MysteryBoxAssemblerFactory.create
import ui.GenerateMysteryBoxDialog
import ui.MysteryBoxDialogResult
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.table.DefaultTableModel

class MysteryBoxGenerateButtonListener(
    private val config: Config,
    private val catalogue: Catalogue,
    private val mysteryBoxList: MysteryBoxList,
    private val catalogueDtm: DefaultTableModel,
    private val mysteryDtm: DefaultTableModel
) : ActionListener {
    override fun actionPerformed(e: ActionEvent?) {
        val result = GenerateMysteryBoxDialog.openDialog(config, catalogue) ?: return
        val mb = addItemToDtm(result)
        adjustCatalogue(mb)
    }

    private fun addItemToDtm(result: MysteryBoxDialogResult): MysteryBox {
        val assembler = create(config, catalogue, result)
        val mysteryBox = assembler.generateBox()
        mysteryDtm.addRow(mysteryBox.toTableArray())
        mysteryBoxList.addBox(mysteryBox)
        return mysteryBox
    }

    private fun adjustCatalogue(mysteryBox: MysteryBox) {
        mysteryBox.items.forEach { b ->
            val newEntry = catalogue.updateQuantity(b.title) { it - 1 } ?: return
            val i = catalogueDtm.dataVector.indexOfFirst { v -> v[0] == b.title }
            catalogueDtm.dataVector[i] = newEntry.toTableVector()
        }


    }
}