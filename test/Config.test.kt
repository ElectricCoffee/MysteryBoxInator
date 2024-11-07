import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class ConfigTest {
    @Test fun `default config generates as expected`() {
        val actual = defaultConfig.toFileString();

        val expected = """
            |[io]
            |outputDirectory = "${System.getProperty("user.home").replace("\\", "\\\\")}"
            |csvDelimiter = ","
            |[mysteryBox.small]
            |price = 45
            |percentage = 20
            |[mysteryBox.large]
            |price = 135
            |percentage = 50
            |[mysteryBox.medium]
            |price = 90
            |percentage = 30
            |
        """.trimMargin()

        assertEquals(expected, actual)
    }

    @Test fun `custom config is invariant to inline types`() {
        val config = """
            |io = { outputDirectory = "~/foo", csvDelimiter = ";" }
            |
            |[mysteryBox]
            |foo = {price = 2.95, percentage = 50}
            |bar = {price = 3.95, percentage = 50}
        """.trimMargin()

        val expected = Config(
            IoConfig("~/foo", ";"),
            mapOf(
                "foo" to MysteryBoxAmount(BigDecimal("2.95"), BigDecimal(50)),
                "bar" to MysteryBoxAmount(BigDecimal("3.95"), BigDecimal(50))))

        val actual = Config.fromToml(config)

        assertEquals(expected, actual);
    }
}