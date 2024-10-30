// NB: targetValue is in pennies.
open class MysteryBox(val targetValue: Int, val boxType: GameCategory) {
    var currentValue = 0
        private set;

    val items = mutableListOf<Game>();

    // TODO: add box type to the inclusion process.... somehow
    fun addItem(item: Game): ItemAddStatus {
        val newCurrent = currentValue + item.value;

        if (newCurrent > targetValue) {
            return ItemAddStatus.EXCEEDS_TARGET_VALUE;
        }

        items.add(item);
        currentValue = newCurrent
        return ItemAddStatus.SUCCESS;
    }
}

class LargeMysteryBox(boxType: GameCategory) : MysteryBox(13500, boxType)
class MediumMysteryBox(boxType: GameCategory) : MysteryBox(9000, boxType)
class SmallMysteryBox(boxType: GameCategory) : MysteryBox(4500, boxType)