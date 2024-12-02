package util

import java.math.BigDecimal
import java.math.RoundingMode

object NumUtils {

    /** Converts a number between 0 and 100 to a number between 0 and 1 */
    fun toFraction(bigDecimal: BigDecimal) = (bigDecimal.setScale(2) / BigDecimal("100.00")).setScale(2)

    fun asPrice(num: BigDecimal) = "£" + num.setScale(2, RoundingMode.HALF_UP).toString()
    fun asPercentage(num: BigDecimal) = (num * BigDecimal("100.00")).setScale(2, RoundingMode.HALF_UP).toString() + "%"
}