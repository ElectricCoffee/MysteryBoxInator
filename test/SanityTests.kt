import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

// these tests are just to test language features I'm unsure about just for my own sanity.
class SanityTests {
    @Test fun `pass by reference works as expected`() {
        val lst = mutableListOf(1, 2, 3, 4, 5)

        fun removeItem(xs: MutableList<Int>, x: Int) {
            xs.remove(x)
        }

        removeItem(lst, 2)

        Assertions.assertFalse(lst.contains(2), "Expect the list to no longer contain the removed item")
    }
}