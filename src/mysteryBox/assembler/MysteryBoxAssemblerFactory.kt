package mysteryBox.assembler

import catalogue.Catalogue
import common.GameCategory
import config.Config
import ui.MysteryBoxDialogResult
import java.math.BigDecimal

// it's happening... I've gone full Java
object MysteryBoxAssemblerFactory {
    @JvmStatic
    fun create(config: Config, catalogue: Catalogue, type: GameCategory, budget: BigDecimal, shortLabel: String, excludeVariety: Boolean): MysteryBoxAssemblerABC {
        return when (type) {
            GameCategory.VARIETY -> VarietyBoxAssembler(config, catalogue, budget, shortLabel, excludeVariety)
            GameCategory.TRICK_TAKER -> TrickTakingBoxAssembler(config, catalogue, budget, shortLabel, excludeVariety)
            GameCategory.ACCESSORY -> throw Exception("You can't yet make Accessory boxes.")
        }
    }

    @JvmStatic
    fun create(config: Config, catalogue: Catalogue, result: MysteryBoxDialogResult) =
        create(config, catalogue, result.type, result.price, result.shortLabel, result.excludeOther)
}