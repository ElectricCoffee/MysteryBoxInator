package ui

import common.GameCategory
import java.math.BigDecimal

class MysteryBoxDialogResult(val price: BigDecimal, val shortLabel: String, val type: GameCategory, val excludeOther: Boolean)