import java.math.BigDecimal
import kotlin.math.roundToInt

// NB: targetValue is in pennies.
open class MysteryBox(val targetValue: BigDecimal, val boxType: GameCategory) {
    var currentValue = BigDecimal.ZERO
        private set;

    val items = mutableListOf<Game>();

    val totalProfit: BigDecimal
        get() = items.sumOf { it.profit }

    private val targetValueUpperLimit = (targetValue + (targetValue * BigDecimal("0.10")))
    private val targetValueLowerLimit = (targetValue - (targetValue * BigDecimal("0.05"))) // find use for this.

    // TODO: add box type to the inclusion process.... somehow
    fun addItem(item: Game): ItemAddStatus {
        val newCurrent = currentValue + item.retailValue;

        if (newCurrent > targetValueUpperLimit) {
            return ItemAddStatus.EXCEEDS_TARGET_VALUE;
        }

        items.add(item);
        currentValue = newCurrent
        return ItemAddStatus.SUCCESS;
    }
}