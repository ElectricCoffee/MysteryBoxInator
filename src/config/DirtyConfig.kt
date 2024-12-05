package config

import catalogue.Catalogue
import com.moandjiezana.toml.Toml
import errors.ConfigMissingException
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Path

// "dirty" versions of the config. Freshly loaded from the toml file and subject to having null values
// all of these will need to be validated before they can be turned into the real config

data class DirtyIoConfig(val outputDirectory: String?, val csvDelimiter: String?) {
    @Throws(ConfigMissingException::class)
    fun validate(): IoConfig {
        if (outputDirectory == null) throw ConfigMissingException("io.outputDirectory")
        if (csvDelimiter == null) throw ConfigMissingException("io.csvDelimiter")
        return IoConfig(outputDirectory, csvDelimiter)
    }
}

data class DirtyThresholdConfig(val upperBound: BigDecimal?, val lowerBound: BigDecimal?) {
    @Throws(ConfigMissingException::class)
    fun validate(): ThresholdConfig {
        if (upperBound == null) throw ConfigMissingException("thresholds.upperBound")
        if (lowerBound == null) throw ConfigMissingException("thresholds.lowerBound")
        return ThresholdConfig(upperBound, lowerBound)
    }
}

data class DirtyRarityRatio(val common: Double?, val uncommon: Double?, val rare: Double?) {
    @Throws(ConfigMissingException::class)
    fun validate(): RarityRatio {
        if (common == null) throw ConfigMissingException("rarityRatio.common")
        if (uncommon == null) throw ConfigMissingException("rarityRatio.uncommon")
        if (rare == null) throw ConfigMissingException("rarityRatio.rare")
        return RarityRatio(common, uncommon, rare)
    }
}

data class DirtyMysteryBoxAmount(val price: BigDecimal?, val shortLabel: String?) {
    @Throws(ConfigMissingException::class)
    fun validate(name: String): MysteryBoxAmount {
        if (price == null) throw ConfigMissingException("$name.price")
        if (shortLabel == null) throw ConfigMissingException("$name.shortLabel")
        return MysteryBoxAmount(price, shortLabel)
    }
}

data class DirtyConfig(
    val io: DirtyIoConfig?,
    val thresholds: DirtyThresholdConfig?,
    val rarityRatio: DirtyRarityRatio?,
    val mysteryBox: Map<String, DirtyMysteryBoxAmount>?
) {
    @Throws(ConfigMissingException::class)
    fun validate(): Config {
        if (io == null) throw ConfigMissingException("io")
        if (thresholds == null) throw ConfigMissingException("thresholds")
        if (rarityRatio == null) throw ConfigMissingException("rarityRatio")
        if (mysteryBox == null) throw ConfigMissingException("mysteryBox")

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