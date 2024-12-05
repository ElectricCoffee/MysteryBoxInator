package config

import catalogue.Catalogue
import com.moandjiezana.toml.Toml
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Path

// "dirty" versions of the config. Freshly loaded from the toml file and subject to having null values
// all of these will need to be validated before they can be turned into the real config

data class DirtyIoConfig(val outputDirectory: String?, val csvDelimiter: String?) {
    fun validate(): IoConfig {
        requireNotNull(outputDirectory) {"io.outputDirectory missing in config"}
        requireNotNull(csvDelimiter) {"io.csvDelimiter missing in config"}
        return IoConfig(outputDirectory, csvDelimiter)
    }
}

data class DirtyThresholdConfig(val upperBound: BigDecimal?, val lowerBound: BigDecimal?) {
    fun validate(): ThresholdConfig {
        requireNotNull(upperBound) {"thresholds.upperBound missing in config"}
        requireNotNull(lowerBound) {"thresholds.lowerBound missing in config"}
        return ThresholdConfig(upperBound, lowerBound)
    }
}

data class DirtyRarityRatio(val common: Double?, val uncommon: Double?, val rare: Double?) {
    fun validate(): RarityRatio {
        requireNotNull(common) {"rarityRatio.common missing in Config"}
        requireNotNull(uncommon) {"rarityRatio.uncommon missing in Config"}
        requireNotNull(rare) {"rarityRatio.rare missing in Config"}
        return RarityRatio(common, uncommon, rare)
    }
}

data class DirtyMysteryBoxAmount(val price: BigDecimal?, val shortLabel: String?) {
    fun validate(name: String): MysteryBoxAmount {
        requireNotNull(price) {"$name.price missing in config"}
        requireNotNull(shortLabel) {"$name.shortLabel missing in config"}
        return MysteryBoxAmount(price, shortLabel)
    }
}

data class DirtyConfig(
    val io: DirtyIoConfig?,
    val thresholds: DirtyThresholdConfig?,
    val rarityRatio: DirtyRarityRatio?,
    val mysteryBox: Map<String, DirtyMysteryBoxAmount>?
) {
    fun validate(): Config {
        requireNotNull(io) {"io missing in config"}
        requireNotNull(thresholds) {"thresholds missing in config"}
        requireNotNull(rarityRatio) {"rarityRatio missing in config"}
        requireNotNull(mysteryBox) {"mysteryBox missing in config"}

        val clean = mysteryBox.mapValues { (k, v) -> v.validate(k) }

        return Config(io.validate(), thresholds.validate(), rarityRatio.validate(), clean)
    }

    companion object {
        fun fromToml(toml: String): DirtyConfig {
            return Toml().read(toml).to(DirtyConfig::class.java)
        }

        fun fromFile(path: Path): DirtyConfig {
            val content = Files.readAllLines(path).joinToString("\n")
            return fromToml(content)
        }
    }
}