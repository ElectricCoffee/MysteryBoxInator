package util

import kotlin.random.Random

object RandUtils {
    // todo: make a random picker util
    fun <A>pickRandom(lst: List<A>): A? {
        val len = lst.count()
        if (len == 0) return null
        val index = Random.nextInt(len)
        val outputValue = lst[index]
        return outputValue
    }
}