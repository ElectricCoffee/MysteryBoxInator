import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Path

data class ThresholdConfig(val upperBound: BigDecimal, val lowerBound: BigDecimal) {
    /**
     * Converts the percentage to a decimal number between 0 and 1
     */
    val lowerAsFraction: BigDecimal
        get() = lowerBound / BigDecimal(100)

    /**
     * Converts the percentage to a decimal number between 0 and 1
     */
    val upperAsFraction: BigDecimal
        get() = upperBound / BigDecimal(100)
}

data class IoConfig(val outputDirectory: String, val csvDelimiter: String)

data class MysteryBoxAmount(val price: BigDecimal, val percentage: BigDecimal) {
    val percentageAsFraction: BigDecimal
        get() = percentage / BigDecimal(100)
}

data class Config(val io: IoConfig, val thresholds: ThresholdConfig, val mysteryBox: Map<String, MysteryBoxAmount>) {
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

val defaultConfigString = """
    |# NB: lines starting with # are comments, they're just there to tell you how to use the config.
    |# Lines that have text inside [] are the various categories you can configure. 
    |# Right now the only configurable parts are 
    |# - "io" which controls input-output stuff, like file paths and such and 
    |# - "mysteryBox", which sets the name for each of the box sizes you want, 
    |[io]
    |   # By default this is your home folder. The one with the same name as your username on the computer.
    |   # On Windows this is usually C:\Users\<username>\.
    |   # If you want it to be your desktop, add \Desktop to the end (within the quotes).
    |   outputDirectory = '''${System.getProperty("user.home")}'''
    |   
    |   # The delimiter is what you separate the csv entries by. The default is comma (,) but you can change it to anything
    |   csvDelimiter = ","
    |   
    |# The thresholds are how much the price can vary by in percents
    |[thresholds]
    |   upperBound = 10
    |   lowerBound = 5
    |   
    |# You can create your own categories here. 
    |# They can be small, medium, large, whatever you want.
    |# The only requirement is that they have a price and a percentage, and that each entry starts with "mysteryBox."
    |[mysteryBox.small]
    |   price = 45
    |   percentage = 20
    |[mysteryBox.medium]
    |   price = 90
    |   percentage = 30
    |[mysteryBox.large]
    |   price = 135
    |   percentage = 50
    |   
""".trimMargin()

val defaultConfig = Config.fromToml(defaultConfigString)