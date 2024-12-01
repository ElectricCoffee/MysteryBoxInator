package mysteryBox

import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import config.Config
import java.io.File

class MysteryBoxList() {
    constructor(boxes: List<MysteryBox>) : this() {
        addBoxes(boxes)
    }

    val mysteryBoxes = mutableMapOf<String, MysteryBox>()

    fun addBox(box: MysteryBox) {
        mysteryBoxes[box.id] = box
    }

    fun addBoxes(boxes: List<MysteryBox>) {
        boxes.forEach { addBox(it) }
    }

    fun addBoxes(boxes: Map<String, MysteryBox>) {
        mysteryBoxes.putAll(boxes)
    }

    fun toToml(): String = TomlWriter().write(this)

    companion object {
        fun fromToml(tomlString: String): MysteryBoxList {
            return Toml().read(tomlString).to(MysteryBoxList::class.java)
        }

        fun fromToml(file: File): MysteryBoxList {
            return Toml().read(file).to(MysteryBoxList::class.java)
        }
    }
}