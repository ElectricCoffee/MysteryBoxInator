// distribution of mystery boxes: 50% large, 30% medium, 20% small.

data class MarkedGame(val game: Game) {
    var marked = false
}

class MysteryBoxList(private val catalogue: Catalogue) {
    val mysteryBoxes = mutableListOf<MysteryBox>()

    // TODO: figure out how to add 0-1 trick-takers to the variety category
    // TODO: figure out how to subtract the marked games from the current stock. Should the stock be included in the Game class?
    fun generateMysteryBox(boxSize: MysteryBoxSize, boxType: GameCategory) {
        val list = catalogue.gamesList.filter { it.value.game.gameCategory == boxType }.map { MarkedGame(it.value.game) }.toMutableList()

        val listLength = list.count();

        val mysteryBox =
            when (boxSize) {
                MysteryBoxSize.SMALL -> SmallMysteryBox(boxType)
                MysteryBoxSize.MEDIUM -> MediumMysteryBox(boxType)
                else -> LargeMysteryBox(boxType)
            }

        while (true) {
            val i = (Math.random() * listLength).toInt()

            if (list[i].marked) {
                continue
            }

            val result = mysteryBox.addItem(list[i].game)

            if (result == ItemAddStatus.EXCEEDS_TARGET_VALUE) {
                break
            }

            list[i].marked = true
        }

    }
}