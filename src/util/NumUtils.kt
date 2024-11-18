package util

import java.math.BigDecimal

object NumUtils {

    /** Converts a number between 0 and 100 to a number between 0 and 1 */
    fun toFraction(bigDecimal: BigDecimal) = bigDecimal / BigDecimal(100)
}