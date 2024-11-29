package mysteryBox

import config.Config
import java.util.UUID

class MysteryBoxList(config: Config) {
    val mysteryBoxes = mutableMapOf<UUID, MysteryBox>()

    fun addBox(box: MysteryBox) {
        mysteryBoxes[box.id] = box
    }

    fun addBoxes(boxes: List<MysteryBox>) {
        boxes.forEach { addBox(it) }
    }

    fun addBoxes(boxes: Map<UUID, MysteryBox>) {
        mysteryBoxes.putAll(boxes)
    }
}