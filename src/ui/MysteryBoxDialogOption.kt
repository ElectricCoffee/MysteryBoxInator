package ui

import config.MysteryBoxAmount
import util.NumUtils
import java.util.*

class MysteryBoxDialogOption(private val title: String, private val amount: MysteryBoxAmount) {
    override fun toString(): String {
        return "${title.replaceFirstChar { it.titlecase(Locale.getDefault()) }} (${NumUtils.asPrice(amount.price)})"
    }
}