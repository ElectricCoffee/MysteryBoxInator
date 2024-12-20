import config.*
import errors.ConfigMissingException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException
import java.math.BigDecimal

class ConfigTest {
    @Test fun `default config generates as expected`() {
        val actual = defaultConfig.toFileString();

        val expected = """
            |[io]
            |outputDirectory = "${defaultOutputDir.replace("\\", "\\\\")}"
            |csvDelimiter = ","
            |[thresholds]
            |upperBound = 10
            |lowerBound = 5
            |[rarityRatio]
            |common = 33.33
            |uncommon = 33.33
            |rare = 33.33
            |[mysteryBox.small]
            |price = 45
            |shortLabel = "F"
            |[mysteryBox.large]
            |price = 135
            |shortLabel = "MF"
            |[mysteryBox.medium]
            |price = 90
            |shortLabel = "BF"
            |
        """.trimMargin()

        assertEquals(expected, actual)
    }

    @Test fun `custom config is invariant to inline types`() {
        val config = """
            |io = { outputDirectory = "~/foo", csvDelimiter = ";" }
            |thresholds = { upperBound = 10, lowerBound = 5 }
            |rarityRatio = { common = 10, uncommon = 20, rare = 70 }
            |[mysteryBox]
            |foo = {price = 2.95, shortLabel = "F"}
            |bar = {price = 3.95, shortLabel = "B"}
        """.trimMargin()

        val expected = Config(
            IoConfig("~/foo", ";"),
            ThresholdConfig(BigDecimal(10), BigDecimal(5)),
            RarityRatio(10.0, 20.0, 70.0),
            mapOf(
                "foo" to MysteryBoxAmount(BigDecimal("2.95"), "F"),
                "bar" to MysteryBoxAmount(BigDecimal("3.95"), "B")
            )
        )

        val actual = Config.fromToml(config)

        assertEquals(expected, actual);
    }

    @Test fun `validation fails if properties are missing`() {
        // rarity ratio is missing
        val config = """
            |io = { outputDirectory = "~/foo", csvDelimiter = ";" }
            |thresholds = { upperBound = 10, lowerBound = 5 }
            |[mysteryBox]
            |foo = {price = 2.95, shortLabel = "F"}
            |bar = {price = 3.95, shortLabel = "B"}
        """.trimMargin()

        Assertions.assertThrows(ConfigMissingException::class.java, {Config.fromToml(config)})
    }
}