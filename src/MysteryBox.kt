import kotlin.math.roundToInt

// NB: targetValue is in pennies.
open class MysteryBox(val targetValue: Int, val boxType: GameCategory) {
    var currentValue = 0
        private set;

    val items = mutableListOf<Game>();

    val totalProfit: Int
        get() = items.sumOf { it.profit }

    private val targetValueUpperLimit = (targetValue + (targetValue * 0.1)).roundToInt()
    private val targetValueLowerLimit = (targetValue - (targetValue * 0.05)).roundToInt() // find use for this.

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