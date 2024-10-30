import kotlin.math.roundToInt

// NB: targetValue is in pennies.
open class MysteryBox(val targetValue: Int, val boxType: GameCategory, val size: MysteryBoxSize) {
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

// TODO: make these editable in config.
class LargeMysteryBox(boxType: GameCategory)
    : MysteryBox(13500, boxType, MysteryBoxSize.LARGE)

class MediumMysteryBox(boxType: GameCategory)
    : MysteryBox(9000, boxType, MysteryBoxSize.MEDIUM)

class SmallMysteryBox(boxType: GameCategory)
    : MysteryBox(4500, boxType, MysteryBoxSize.SMALL)