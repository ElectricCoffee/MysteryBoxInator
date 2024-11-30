package mysteryBox.assembler

import catalogue.Catalogue
import common.GameCategory
import config.Config
import ui.MysteryBoxDialogResult
import java.math.BigDecimal

// it's happening... I've gone full Java
object MysteryBoxAssemblerFactory {
    @JvmStatic
    fun create(config: Config, catalogue: Catalogue, type: GameCategory, value: BigDecimal, excludeVariety: Boolean): MysteryBoxAssemblerABC {
        return when (type) {
            GameCategory.VARIETY -> VarietyBoxAssembler(config, catalogue, value, excludeVariety)
            GameCategory.TRICK_TAKER -> TrickTakingBoxAssembler(config, catalogue, value, excludeVariety)
            GameCategory.ACCESSORY -> throw Exception("You can't yet make Accessory boxes.")
        }
    }

    @JvmStatic
    fun create(config: Config, catalogue: Catalogue, result: MysteryBoxDialogResult) =
        create(config, catalogue, result.type, result.price, result.excludeOther)
}