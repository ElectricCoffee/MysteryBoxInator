import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Path

data class IoConfig(val outputDirectory: String, val csvDelimiter: String)

data class MysteryBoxAmount(val price: BigDecimal, val percentage: BigDecimal)

data class MysteryBoxConfig(val sizes: Map<String, MysteryBoxAmount>)

data class Config(val io: IoConfig, val mysteryBox: MysteryBoxConfig) {
    fun toFileString(): String {
        val writer = TomlWriter()
        return writer.write(this)
    }

    companion object {
        fun fromToml(toml: String): Config {
            return Toml().read(toml).to(Config::class.java)
        }

        fun fromFile(path: Path): Config {
            val content = Files.readAllLines(path).joinToString("\n")
            return fromToml(content)
        }
    }
}

val defaultConfig = Config(
    IoConfig("~", ","),
    MysteryBoxConfig(
        mapOf(
            "small" to MysteryBoxAmount(BigDecimal(45), BigDecimal(20)),
            "medium" to MysteryBoxAmount(BigDecimal(90), BigDecimal(30)),
            "large" to MysteryBoxAmount(BigDecimal(135), BigDecimal(50)))
    )
)