package mysteryBox.assembler

import common.Budget
import game.Game
import common.GameRarity
import common.ItemPickStatus
import catalogue.Catalogue
import common.GameCategory
import config.Config
import mysteryBox.MysteryBox
import util.RandUtils
import java.math.BigDecimal
import kotlin.random.Random

// strategy:
// base class splits the catalogue into three lists of data
// then we have three child classes that each uniquely handle the assembly of a mystery box
// these child classes have local variables that keep track of things like number of boxes pulled, the target price, and the thresholds

// ABC short for Abstract Base Class
abstract class MysteryBoxAssemblerABC(
    protected val config: Config,
    private val catalogue: Catalogue,
    protected val budget: BigDecimal,
    protected val shortLabel: String)
{
    protected val accessories: MutableList<Game> = mutableListOf()
    protected val trickTakers: MutableList<Game> = mutableListOf()
    protected val varieties: MutableList<Game> = mutableListOf()
    protected var moneySpent = BigDecimal.ZERO
    protected var hasPickedTrickTaker = false
    protected var hasPickedAccessory = false
    protected var hasPickedVariety = false
    protected var hasPickedMythic = false

    protected val upperLimit: BigDecimal
        get() = (config.thresholds.upperAsFraction + (1).toBigDecimal()) * budget

    protected val lowerLimit: BigDecimal
        get() = budget - (config.thresholds.lowerAsFraction * budget)

    protected val pickedItems = mutableListOf<Game>()

    protected val pickedCount get() = pickedItems.count().toDouble()

    protected val moneyLeft: BigDecimal
        get() = budget - moneySpent

    protected val moneyLeftUpper: BigDecimal
        get() = upperLimit - moneySpent

    // nb: is a number between 0 and 1
    private val pctCommon: Double
        get() {
            if (pickedCount == 0.0) return 0.0
            return pickedItems.count { it.rarity == GameRarity.COMMON }.toDouble() / pickedCount
        }

    private val pctUncommon: Double
        get() {
            if (pickedCount == 0.0) return 0.0
            return pickedItems.count { it.rarity == GameRarity.UNCOMMON }.toDouble() / pickedItems.count().toDouble()
        }

    // nb: we're counting mythics as rare here
    private val pctRare: Double
        get() {
            if (pickedCount == 0.0) return 0.0
            return pickedItems.count { it.rarity == GameRarity.RARE || it.rarity == GameRarity.MYTHIC }.toDouble() / pickedCount
        }

    private fun countCommon(games: List<Game>) = games.count { it.rarity == GameRarity.COMMON }
    private fun countUncommon(games: List<Game>) = games.count { it.rarity == GameRarity.UNCOMMON }
    private fun countRare(games: List<Game>) = games.count { it.rarity == GameRarity.RARE }
    private fun countMythic(games: List<Game>) = games.count { it.rarity == GameRarity.MYTHIC }

    /**
     * Checks if the value we want to pick is within budget
     * @param value the value of the item we wish to check
     * @param checkUpperLimit if true, we check the money left adjusted for the n% upper limit
     */
    private fun withinBudget(value: BigDecimal, checkUpperLimit: Boolean) =
        if (!checkUpperLimit) value <= moneyLeft else value <= moneyLeftUpper

    /**
     * Picks an item from the list of games, making sure it stays within budget
     * @param items is the list of items to be fetched from
     * @param checkUpperLimit causes the function to use the moneyLeftUpper property instead of moneyLeft in its calculations
     * @param filter if used, cause it to filter not only on the money left in budget, but also this one extra filter
     * @return an ItemPickStatus depending on the state of the items provided
     */
    private fun pickHelper(items: MutableList<Game>, checkUpperLimit: Boolean = false, filter: (Game) -> Boolean = { true }): ItemPickStatus {
        if (items.isEmpty()) return ItemPickStatus.FAILURE_NO_ITEMS;
        val filteredList = items.filter { filter(it) && withinBudget(it.retailValue, checkUpperLimit) }
//        println(filteredList.map { "${it.title} ${it.rarity}" })
        val item = RandUtils.pickRandom(filteredList)

        if (item == null) {
            return if (checkUpperLimit) {
                ItemPickStatus.FAILURE_NOTHING_AVAILABLE_AT_RAISED_BUDGET
            } else {
                ItemPickStatus.FAILURE_NOTHING_AFFORDABLE_AT_NORMAL_BUDGET
            }
        }

        pickedItems.add(item)
        items.remove(item)
        moneySpent += item.retailValue
        return ItemPickStatus.SUCCESS
    }

    protected fun pickTrickTaker(checkUpperLimit: Boolean = false) =
        pickHelper(trickTakers, checkUpperLimit)

    protected fun pickAccessory(checkUpperLimit: Boolean = false) =
        pickHelper(accessories, checkUpperLimit)

    protected fun pickVariety(checkUpperLimit: Boolean = false) =
        pickHelper(varieties, checkUpperLimit)

    protected fun pickMythic(items: MutableList<Game>, checkUpperLimit: Boolean = false) =
        pickHelper(items, checkUpperLimit) { it.rarity == GameRarity.MYTHIC }

    protected fun pickRare(items: MutableList<Game>, checkUpperLimit: Boolean = false) =
        pickHelper(items, checkUpperLimit) { it.rarity == GameRarity.RARE }

    protected fun pickUncommon(items: MutableList<Game>, checkUpperLimit: Boolean = false) =
        pickHelper(items, checkUpperLimit) { it.rarity == GameRarity.UNCOMMON }

    protected fun pickCommon(items: MutableList<Game>, checkUpperLimit: Boolean = false) =
        pickHelper(items, checkUpperLimit) { it.rarity == GameRarity.COMMON }

    protected fun pickSpecialMythic(items: MutableList<Game>, checkUpperLimit: Boolean = false) {
            if (!hasPickedMythic && includeSpecial()) {
                pickMythic(items, checkUpperLimit)
            }
            hasPickedMythic = true
    }

    protected fun pickSpecialTrickTaker(checkUpperLimit: Boolean = false) {
        try {
            if (!hasPickedTrickTaker && trickTakers.isEmpty()) {
                return;
            }

            if (!hasPickedTrickTaker && includeSpecial()) {
                pickTrickTaker(checkUpperLimit)
            }
        } finally {
            hasPickedTrickTaker = true
        }
    }
    protected fun pickSpecialVariety(checkUpperLimit: Boolean = false) {
        try {
            if (!hasPickedVariety && accessories.isEmpty()) {
                return
            }

            if (!hasPickedVariety && includeSpecial()) {
                pickAccessory(checkUpperLimit)
            }

        } finally {
            hasPickedVariety = true
        }
    }
    protected fun pickSpecialAccessory(checkUpperLimit: Boolean = false) {
        try {
            if (!hasPickedAccessory && accessories.isEmpty()) {
                return
            }

            if (!hasPickedAccessory && includeSpecial()) {
                pickAccessory(checkUpperLimit)
            }

        } finally {
            hasPickedAccessory = true
        }
    }

    // 1 in 8 chance of returning true. If true, we should add one of the special items to the mystery box
    protected fun includeSpecial() = Random.nextInt(8) == 0

    /**
     * Picks the next game rarity to put into the mystery box based on the desired percentages as configured in the config file.
     * The way it does this is by comparing the current box percentage with the expected percentage and subtracts the two numbers.
     * If the number is positive, then we have "too many" of that item in the box. If the number is negative we have "too few".
     * We then figure out the minimum value amongst the percentages.
     *
     * If a given category has no items remaining, we set that metric to infinity, thus making it so it's never picked
     */
    protected fun pickNext(lst: List<Game>): GameRarity {
        val ratio = config.rarityRatio
        // if the count is 0, set the metric to +∞, else calculate the metric as normal
        fun calcMetric(counter: (g: List<Game>) -> Int, lst: List<Game>, calc: () -> Double) = if (counter(lst) > 0) calc() else Double.POSITIVE_INFINITY

//        println(pickedItems)
//        println("$pctCommon, $pctUncommon, $pctRare, $ratio")

        // nb: if the values are positive, it means we are above the desired ratio, and if negative it means we are under
        val common = Pair(GameRarity.COMMON, calcMetric(::countCommon, lst) { pctCommon - ratio.commonAsFraction })
        val uncommon = Pair(GameRarity.UNCOMMON, calcMetric(::countUncommon, lst) { pctUncommon - ratio.uncommonAsFraction })
        val rare = Pair(GameRarity.RARE, calcMetric(::countRare, lst) { pctRare - ratio.rareAsFraction })

//        println("metrics: $common, $uncommon, $rare, ${Double.POSITIVE_INFINITY}")

        // cursed and nsfw implementation of minimum because the built-in doesn't do what I want.
        val result = listOf(common, uncommon, rare)
            .fold(Pair(GameRarity.COMMON, Double.POSITIVE_INFINITY)) { a, b -> if (a.second < b.second) a else b }
                .first
//        println("Picking $result")
        return result
    }

    init {
        catalogue.gamesList.filter { it.value.quantity > 0 }.map { it.value.game }.forEach {
            when (it.gameCategory) {
                GameCategory.ACCESSORY -> accessories.add(it)
                GameCategory.TRICK_TAKER -> trickTakers.add(it)
                GameCategory.VARIETY -> varieties.add(it)
            }
        }
    }

    protected fun budgetStatus(): Budget {
        return Budget.fromMoneySpent(moneySpent, budget)
    }

    abstract fun generateBox(): MysteryBox
}