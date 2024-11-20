package mysteryBox

import Budget
import Game
import GameRarity
import ItemPickStatus
import catalogue.Catalogue
import config.Config
import util.RandUtils
import java.math.BigDecimal
import kotlin.random.Random

// strategy:
// base class splits the catalogue into three lists of data
// then we have three child classes that each uniquely handle the assembly of a mystery box
// these child classes have local variables that keep track of things like number of boxes pulled, the target price, and the thresholds

abstract class MysteryBoxAssembler(protected val config: Config, private val catalogue: Catalogue, protected val budget: BigDecimal) {
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

    protected val moneyLeft: BigDecimal
        get() = budget - moneySpent

    protected val moneyLeftUpper: BigDecimal
        get() = upperLimit - moneySpent

    // nb: is a number between 0 and 1
    protected val pctCommon: Double
        get() = pickedItems.count { it.rarity == GameRarity.COMMON }.toDouble() / pickedItems.count().toDouble()

    protected val pctUncommon: Double
        get() = pickedItems.count { it.rarity == GameRarity.UNCOMMON }.toDouble() / pickedItems.count().toDouble()

    // nb: we're counting mythics as rare here
    protected val pctRare: Double
        get() = pickedItems.count { it.rarity == GameRarity.RARE || it.rarity == GameRarity.MYTHIC }.toDouble() / pickedItems.count().toDouble()

    private fun withinBudget(value: BigDecimal, checkUpperLimit: Boolean) = if (!checkUpperLimit) value <= moneyLeft else value <= moneyLeftUpper

    private fun pickHelper(items: MutableList<Game>, checkUpperLimit: Boolean = false, filter: (Game) -> Boolean = { true }): ItemPickStatus {
        if (items.isEmpty()) return ItemPickStatus.FAILURE_NO_ITEMS;
        val item = RandUtils.pickRandom(items.filter { filter(it) && withinBudget(it.retailValue, checkUpperLimit) }) ?: return ItemPickStatus.FAILURE_NOTHING_AFFORDABLE_AT_NORMAL_BUDGET

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
     */
    protected fun pickNext(): GameRarity {
        val ratio = config.rarityRatio
        // nb: if the values are positive, it means we are above the desired ratio, and if negative it means we are under
        val common = Pair(GameRarity.COMMON, pctCommon - ratio.commonAsFraction)
        val uncommon = Pair(GameRarity.UNCOMMON, pctUncommon - ratio.uncommonAsFraction)
        val rare = Pair(GameRarity.RARE, pctRare - ratio.rareAsFraction)

        // cursed and nsfw implementation of minimum because the built-in doesn't do what I want.
        return listOf(common, uncommon, rare)
            .fold(Pair(GameRarity.COMMON, Double.POSITIVE_INFINITY)) { a, b -> if (a.second < b.second) a else b }
                .first
    }

    init {
        catalogue.gamesList.map { it.value.game }.forEach {
            when (it.gameCategory) {
                GameCategory.ACCESSORY -> accessories.add(it)
                GameCategory.TRICK_TAKER -> trickTakers.add(it)
                GameCategory.VARIETY -> varieties.add(it)
            }
        }
    }

    protected fun budgetStatus(): Budget {
        return if (moneySpent <= lowerLimit) {
            Budget.UnderBudget(moneySpent / budget)
        } else if (moneySpent >= upperLimit) {
            Budget.OverBudget(moneySpent / budget)
        } else {
            Budget.OnBudget
        }
    }

    abstract fun generateBox(): MysteryBox
}

class VarietyBoxAssembler(config: Config, catalogue: Catalogue, value: BigDecimal, excludeTrickTaker: Boolean = false)
    : MysteryBoxAssembler(config, catalogue, value) {

    init {
        hasPickedTrickTaker = excludeTrickTaker
    }

    private fun pickSpecials() {
        pickSpecialMythic(varieties)
        pickSpecialTrickTaker()
        pickSpecialAccessory()
    }

    override fun generateBox(): MysteryBox {
        pickSpecials()

        var tryAgainOverBudget = false

        while(true) {
            val result = when (pickNext()) {
                GameRarity.COMMON -> pickCommon(varieties, tryAgainOverBudget)
                GameRarity.UNCOMMON -> pickUncommon(varieties, tryAgainOverBudget)
                GameRarity.RARE, GameRarity.MYTHIC -> pickRare(varieties, tryAgainOverBudget)
            }

            when (result) {
                ItemPickStatus.SUCCESS -> continue
                ItemPickStatus.FAILURE_NO_ITEMS -> break
                ItemPickStatus.FAILURE_NOTHING_AFFORDABLE_AT_NORMAL_BUDGET -> {
                    tryAgainOverBudget = true
                    continue
                }
                ItemPickStatus.FAILURE_NOTHING_AVAILABLE_AT_RAISED_BUDGET -> {
                    break
                }
            }
        }

        return MysteryBox(pickedItems, budget, GameCategory.VARIETY, budgetStatus())
    }
}