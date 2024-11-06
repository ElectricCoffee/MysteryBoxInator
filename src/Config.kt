import com.moandjiezana.toml.Toml
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Path

data class IoConfig(val outputDirectory: String, val csvDelimiter: String)

data class MysteryBoxConfig(val sizes: Map<String, BigDecimal>)

data class Config(val io: IoConfig, val mysteryBox: MysteryBoxConfig) {
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
        mapOf("small" to BigDecimal(45), "medium" to BigDecimal(90), "large" to BigDecimal(135))
    )
)