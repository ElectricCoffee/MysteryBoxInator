package util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ListUtilTests {
    @Test fun `list utils can pull out a random item from a list and leave a list without it`() {
        val testList = listOf(1, 2, 3, 4, 5, 6)
        val (item, lst) = ListUtils.pickRandom(testList)

        Assertions.assertTrue(testList.contains(item), "The original list still contains the item")
        Assertions.assertFalse(lst.contains(item), "The resulting list does not contain the item")
        Assertions.assertTrue(testList.count() > lst.count(), "The original list is longer than the new one")
    }
}