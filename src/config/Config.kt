package config

import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import util.NumUtils
import java.io.File
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Path


data class CatalogueConfig(val deleteProductWhenZeroInventory: Boolean)

data class ThresholdConfig(val upperBound: BigDecimal, val lowerBound: BigDecimal) {
    /**
     * Converts the percentage to a decimal number between 0 and 1
     */
    val lowerAsFraction: BigDecimal
        get() = NumUtils.toFraction(lowerBound)

    /**
     * Converts the percentage to a decimal number between 0 and 1
     */
    val upperAsFraction: BigDecimal
        get() = NumUtils.toFraction(upperBound)
}

data class IoConfig(val outputDirectory: String, val csvDelimiter: String)

data class MysteryBoxAmount(val price: BigDecimal, val shortLabel: String)

data class RarityRatio(val common: Double, val uncommon: Double, val rare: Double) {
    val commonAsFraction: Double
        get() = common / 100

    val uncommonAsFraction: Double
        get() = uncommon / 100

    val rareAsFraction: Double
        get() = rare / 100
}

data class Config(
    val io: IoConfig,
    val thresholds: ThresholdConfig,
    val catalogue: CatalogueConfig,
    val rarityRatio: RarityRatio,
    val mysteryBox: Map<String, MysteryBoxAmount>
) {
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

@JvmField
val configFolderPath = System.getProperty("user.home") + File.separator + ".mystery-box-inator"

@JvmField
val configFilePath = configFolderPath + File.separator + "config.toml"

@JvmField
val defaultOutputDir = System.getProperty("user.home") + File.separator + "MysteryBoxInator"

@JvmField
val defaultConfigString = """
    |# NB: lines starting with # are comments, they're just there to tell you how to use the config.
    |# Lines that have text inside [] are the various categories you can configure. 
    |# Right now the only configurable parts are 
    |# - "io" which controls input-output stuff, like file paths and such and 
    |# - "mysteryBox", which sets the name for each of the box sizes you want, 
    |[io]
    |   # By default this is the MysteryBoxInator folder inside your your home folder.
    |   # On Windows the home folder is C:\Users\<username>\.
    |   # If you want it to be your desktop, add \Desktop to the end (within the quotes).
    |   outputDirectory = '''$defaultOutputDir'''
    |   
    |   # The delimiter is what you separate the csv entries by. The default is comma (,) but you can change it to anything
    |   csvDelimiter = ","
    |   
    |# The thresholds are how much the price can vary by in percents
    |[thresholds]
    |   upperBound = 10
    |   lowerBound = 5
    |   
    |[catalogue]
    |   # if true, the program will remove a product entry entirely when the stock reaches zero
    |   deleteProductWhenZeroInventory = false
    |   
    |[rarityRatio]
    |   # sets the distribution of each of the various rarities. By default they're all at 33.33% (about 1/3)
    |   common = 33.33
    |   uncommon = 33.33
    |   rare = 33.33
    |   
    |# You can create your own categories here. 
    |# They can be small, medium, large, whatever you want.
    |# The only requirement is that they have a price, and that each entry starts with "mysteryBox."
    |# The shortLabel is what's used when exporting the mystery box
    |[mysteryBox.small]
    |   price = 45
    |   shortLabel = "F"
    |[mysteryBox.medium]
    |   price = 90
    |   shortLabel = "BF"
    |[mysteryBox.large]
    |   price = 135
    |   shortLabel = "MF"
    |   
""".trimMargin()

@JvmField
val defaultConfig = Config.fromToml(defaultConfigString)