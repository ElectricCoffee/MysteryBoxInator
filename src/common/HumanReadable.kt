package common

import util.NumUtils
import java.math.BigDecimal

/**
 * Human Readable (hr) class wrapper to make serialization a bit easier.
 */
data class HrGameCategory(val category: GameCategory) : Comparable<HrGameCategory> {
    override fun toString(): String {
        return when (category.name) {
            "ACCESSORY" -> "Accessory"
            "TRICK_TAKER" -> "Trick-Taker"
            "VARIETY" -> "Variety"
            else -> category.name // default case just returns the raw name un-prettified.
        }
    }

    fun toShortString(): String {
        return when (category.name) {
            "ACCESSORY" -> "A"
            "TRICK_TAKER" -> "TT"
            "VARIETY" -> "V"
            else -> category.name // default case just returns the raw name un-prettified.
        }
    }

    override fun compareTo(other: HrGameCategory): Int = category.compareTo(other.category)
}

/**
 * Human Readable (hr) class wrapper to make serialization a bit easier.
 */
data class HrGameRarity(val rarity: GameRarity) : Comparable<HrGameRarity> {
    override fun toString(): String {
        return when (rarity.name) {
            "COMMON" -> "Common (${rarity.value})"
            "UNCOMMON" -> "Uncommon (${rarity.value})"
            "RARE" -> "Rare (${rarity.value})"
            "MYTHIC" -> "Mythic (${rarity.value})"
            else -> "${rarity.name} (${rarity.value})"
        }
    }

    override fun compareTo(other: HrGameRarity): Int = rarity.compareTo(other.rarity)
}

data class HrBoolean(val boolean: Boolean) : Comparable<HrBoolean> {
    override fun toString(): String {
        if (boolean) {
            return "Yes"
        } else {
            return "No"
        }
    }

    override fun compareTo(other: HrBoolean): Int = boolean.compareTo(other.boolean)
}

data class HrPrice(val price: BigDecimal) : Comparable<HrPrice> {
    override fun toString(): String {
        return NumUtils.asPrice(price)
    }

    override fun compareTo(other: HrPrice): Int = price.compareTo(other.price)
}