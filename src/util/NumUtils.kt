package util

import java.math.BigDecimal
import java.math.RoundingMode

object NumUtils {

    /** Converts a number between 0 and 100 to a number between 0 and 1 */
    fun toFraction(bigDecimal: BigDecimal) = bigDecimal / BigDecimal(100)

    fun asPrice(num: BigDecimal) = "£" + num.setScale(2, RoundingMode.HALF_UP).toString()
}