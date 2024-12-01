package mysteryBox

import com.moandjiezana.toml.TomlWriter
import config.Config
import java.util.UUID

class MysteryBoxList() {
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

    fun toToml(): String = TomlWriter().write(this)
}