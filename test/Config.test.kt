import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class ConfigTest {
    @Test fun `default config generates as expected`() {
        val writer = TomlWriter()
        val actual = writer.write(defaultConfig);

        val expected = """
            |[io]
            |outputDirectory = "~"
            |csvDelimiter = ","
            |[mysteryBox.sizes]
            |small = 45
            |medium = 90
            |large = 135
            |
        """.trimMargin()

        assertEquals(expected, actual)
    }

    @Test fun `custom config is invariant to inline types`() {
        val config = """
            |io = { outputDirectory = "~/foo", csvDelimiter = ";" }
            |
            |[mysteryBox]
            |sizes = {foo = 2.95, bar = 3.95}
        """.trimMargin()

        val expected = Config(
            IoConfig("~/foo", ";"),
            MysteryBoxConfig(mapOf("foo" to BigDecimal("2.95"), "bar" to BigDecimal("3.95"))))

        val actual = Config.fromToml(config)

        assertEquals(expected, actual);
    }
}