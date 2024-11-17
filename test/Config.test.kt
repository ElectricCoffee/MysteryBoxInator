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
            |[mysteryBox]
            |foo = {price = 2.95, percentage = 50}
            |bar = {price = 3.95, percentage = 50}
        """.trimMargin()

        val expected = Config(
            IoConfig("~/foo", ";"),
            ThresholdConfig(BigDecimal(10), BigDecimal(5)),
            CatalogueConfig(true),
            mapOf(
                "foo" to MysteryBoxAmount(BigDecimal("2.95")),
                "bar" to MysteryBoxAmount(BigDecimal("3.95"))
            )
        )

        val actual = Config.fromToml(config)

        assertEquals(expected, actual);
    }
}