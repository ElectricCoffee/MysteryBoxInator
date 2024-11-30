package ui

import config.MysteryBoxAmount
import util.NumUtils
import java.util.*

class MysteryBoxDialogOption(val title: String, val amount: MysteryBoxAmount) {
    val price get() = amount.price

    override fun toString(): String {
        return "${title.replaceFirstChar { it.titlecase(Locale.getDefault()) }} (${NumUtils.asPrice(price)})"
    }
}