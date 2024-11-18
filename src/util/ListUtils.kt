package util

import kotlin.random.Random

object ListUtils {
    // todo: make a random picker util
    fun <A>pickRandom(lst: List<A>): Pair<A, List<A>> {
        val len = lst.count()
        val index = Random.nextInt(len)
        val outputValue = lst[index]
        val outputList = lst.filterIndexed { i, _ -> i != index }

        return Pair(outputValue, outputList)
    }
}