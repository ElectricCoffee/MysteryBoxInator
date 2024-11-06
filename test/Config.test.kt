import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ConfigTest {
    @Test fun `default config generates as expected`() {
        val writer = TomlWriter()
        val actual = writer.write(defaultConfig);

        val expected = """
            |[io]
            |outputDirectory = "~"
            |csvDelimiter = ","
            |[mysteryBox.sizes]
            |small = 4500
            |medium = 9000
            |large = 13500
            |
        """.trimMargin()

        assertEquals(expected, actual)
    }

    @Test fun `custom config is invariant to inline types`() {
        val config = """
            |io = { outputDirectory = "~/foo", csvDelimiter = ";" }
            |
            |[mysteryBox]
            |sizes = {foo = 2, bar = 3}
        """.trimMargin()

        val expected = Config(IoConfig("~/foo", ";"), MysteryBoxConfig(mapOf("foo" to 2, "bar" to 3)))

        val actual = Config.fromToml(config)

        assertEquals(expected, actual);
    }
}