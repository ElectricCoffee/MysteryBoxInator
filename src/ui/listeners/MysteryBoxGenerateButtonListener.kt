package ui.listeners

import catalogue.Catalogue
import common.GenerateFailedDialogResult
import config.Config
import io.Filing
import mysteryBox.MysteryBox
import mysteryBox.MysteryBoxList
import mysteryBox.assembler.MysteryBoxAssemblerFactory.create
import ui.GenerateMysteryBoxDialog
import ui.MysteryBoxDialogResult
import util.NumUtils
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.math.BigDecimal
import javax.swing.JOptionPane
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
        while (true) {
            when (val mb = addItemToDtm(result)) {
                is GenerateFailedDialogResult.Yes -> return adjustCatalogue(mb.mb)
                is GenerateFailedDialogResult.No -> return
                is GenerateFailedDialogResult.TryAgain -> continue
            }
        }
    }

    private fun addItemToDtm(result: MysteryBoxDialogResult): GenerateFailedDialogResult {
        val assembler = create(config, catalogue, result)
        val mysteryBox = assembler.generateBox()
        var dialogResult: GenerateFailedDialogResult = GenerateFailedDialogResult.Yes(mysteryBox)

        val pct = (mysteryBox.totalValue / mysteryBox.targetValue)
        if (pct <= BigDecimal("1.00") - config.thresholds.lowerAsFraction) {
            val options = arrayOf("Yes", "No", "Try again")
            val res = JOptionPane.showOptionDialog(
                null,
                "Could not find enough games to cover more than ${NumUtils.asPercentage(pct)} of the cost. Keep it anyway?",
                "Could not fill mystery box",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1])
            dialogResult = when(res) {
                0 -> GenerateFailedDialogResult.Yes(mysteryBox)
                1 -> GenerateFailedDialogResult.No
                2 -> GenerateFailedDialogResult.TryAgain
                else -> throw Exception("Unknown result $res")
            }
        }

        if (dialogResult is GenerateFailedDialogResult.Yes) {
            mysteryDtm.addRow(mysteryBox.toTableArray())
            mysteryBoxList.addBox(mysteryBox)
        }

        return dialogResult
    }

    private fun adjustCatalogue(mysteryBox: MysteryBox) {
        mysteryBox.items.forEach { b ->
            val newEntry = catalogue.updateQuantity(b.title) { it - 1 } ?: return
            val i = catalogueDtm.dataVector.indexOfFirst { v -> v[0] == b.title }
            catalogueDtm.dataVector[i] = newEntry.toTableVector()
        }

        Filing.writeWorkingCopy(config, catalogue)
        Filing.writeWorkingCopy(config, mysteryBoxList)
    }
}