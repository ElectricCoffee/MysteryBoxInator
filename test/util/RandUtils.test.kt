package util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RandUtilsTest {
    @Test fun `Rand utils can pull out a random item from a list and leave a list without it`() {
        val testList = listOf(1, 2, 3, 4, 5, 6)
        val item = RandUtils.pickRandom(testList)

        Assertions.assertTrue(testList.contains(item), "The original list still contains the item")
    }
}