import config.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
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
            |[catalogue]
            |deleteProductWhenZeroInventory = false
            |[rarityRatio]
            |common = 33.33
            |uncommon = 33.33
            |rare = 33.33
            |[mysteryBox.small]
            |price = 45
            |[mysteryBox.large]
            |price = 135
            |[mysteryBox.medium]
            |price = 90
            |
        """.trimMargin()

        assertEquals(expected, actual)
    }

    @Test fun `custom config is invariant to inline types`() {
        val config = """
            |io = { outputDirectory = "~/foo", csvDelimiter = ";" }
            |thresholds = { upperBound = 10, lowerBound = 5 }
            |catalogue = { deleteProductWhenZeroInventory = true }
            |rarityRatio = { common = 10, uncommon = 20, rare = 70 }
            |[mysteryBox]
            |foo = {price = 2.95, percentage = 50}
            |bar = {price = 3.95, percentage = 50}
        """.trimMargin()

        val expected = Config(
            IoConfig("~/foo", ";"),
            ThresholdConfig(BigDecimal(10), BigDecimal(5)),
            CatalogueConfig(true),
            RarityRatio(10.0, 20.0, 70.0),
            mapOf(
                "foo" to MysteryBoxAmount(BigDecimal("2.95")),
                "bar" to MysteryBoxAmount(BigDecimal("3.95"))
            )
        )

        val actual = Config.fromToml(config)

        assertEquals(expected, actual);
    }
}