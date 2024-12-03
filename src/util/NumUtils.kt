package util

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

object NumUtils {
    /** Converts a number between 0 and 100 to a number between 0 and 1 */
    @JvmStatic
    fun toFraction(bigDecimal: BigDecimal): BigDecimal = (bigDecimal.setScale(2) / BigDecimal("100.00")).setScale(2)

    @JvmStatic
    fun asPrice(num: BigDecimal): String {
        val format = DecimalFormat("£#,##0.00;-£#,##0.00")
        return format.format(num.setScale(2, RoundingMode.HALF_UP))
    }
    @JvmStatic
    fun asPercentage(num: BigDecimal): String {
        val format = DecimalFormat("#,##0.##%;-#,##0.##%")
        return format.format((num))
    }
    @JvmStatic
    fun asPercentageSigned(num: BigDecimal): String {
        val format = DecimalFormat("+#,##0.##%;-#,##0.##%")
        return format.format((num))
    }
}