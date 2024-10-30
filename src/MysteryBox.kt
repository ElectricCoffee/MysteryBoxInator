class MysteryBox(val targetValue: Int) {
    var currentValue = 0
        private set;

    val items = mutableListOf<Game>();

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