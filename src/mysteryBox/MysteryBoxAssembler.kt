package mysteryBox

import Game
import GameRarity
import catalogue.Catalogue
import config.Config
import util.RandUtils
import java.math.BigDecimal
import kotlin.random.Random

// strategy:
// base class splits the catalogue into three lists of data
// then we have three child classes that each uniquely handle the assembly of a mystery box
// these child classes have local variables that keep track of things like number of boxes pulled, the target price, and the thresholds

enum class AssemblerState {
    START,
    PICK_VARIETY,
    PICK_TRICK_TAKER,
    PICK_ACCESSORY,
}

abstract class MysteryBoxAssembler(protected val config: Config, private val catalogue: Catalogue, protected val value: BigDecimal) {
    protected val accessories: MutableList<Game> = mutableListOf()
    protected val trickTakers: MutableList<Game> = mutableListOf()
    protected val varieties: MutableList<Game> = mutableListOf()
    protected var moneySpent = BigDecimal.ZERO
    protected var hasPickedTrickTaker = false
    protected var hasPickedAccessory = false
    protected var hasPickedVariety = false
    protected var hasPickedMythic = false

    protected val upperLimit: BigDecimal
        get() = (config.thresholds.upperAsFraction + (1).toBigDecimal()) * value

    protected val lowerLimit: BigDecimal
        get() = config.thresholds.lowerAsFraction * value

    protected val pickedItems = mutableListOf<Game>()

    protected val moneyLeft: BigDecimal
        get() = value - moneySpent

    // nb: is a number between 0 and 1
    protected val pctCommon: Double
        get() = pickedItems.count { it.rarity == GameRarity.COMMON }.toDouble() / pickedItems.count().toDouble()

    protected val pctUncommon: Double
        get() = pickedItems.count { it.rarity == GameRarity.UNCOMMON }.toDouble() / pickedItems.count().toDouble()

    // nb: we're counting mythics as rare here
    protected val pctRare: Double
        get() = pickedItems.count { it.rarity == GameRarity.RARE || it.rarity == GameRarity.MYTHIC }.toDouble() / pickedItems.count().toDouble()

    private fun pickHelper(items: MutableList<Game>, filter: (Game) -> Boolean = { true }) {
        if (items.isEmpty()) return;
        val item = RandUtils.pickRandom(items.filter { filter(it) && it.retailValue <= moneyLeft })

        pickedItems.add(item)
        items.remove(item)
        moneySpent += item.retailValue
    }

    protected fun pickTrickTaker() = pickHelper(trickTakers)

    protected fun pickAccessory() = pickHelper(accessories)

    protected fun pickVariety() = pickHelper(varieties)

    protected fun pickMythic(items: MutableList<Game>) = pickHelper(items) { it.rarity == GameRarity.MYTHIC }

    protected fun pickRare(items: MutableList<Game>) = pickHelper(items) { it.rarity == GameRarity.RARE }

    protected fun pickUncommon(items: MutableList<Game>) = pickHelper(items) { it.rarity == GameRarity.UNCOMMON }

    protected fun pickCommon(items: MutableList<Game>) = pickHelper(items) { it.rarity == GameRarity.COMMON }

    protected fun pickSpecialMythic(items: MutableList<Game>) {
            if (!hasPickedMythic && includeSpecial()) {
                pickMythic(items)
            }
            hasPickedMythic = true
    }

    protected fun pickSpecialTrickTaker() {
        try {
            if (!hasPickedTrickTaker && trickTakers.isEmpty()) {
                return;
            }

            if (!hasPickedTrickTaker && includeSpecial()) {
                pickTrickTaker()
            }
        } finally {
            hasPickedTrickTaker = true
        }
    }
    protected fun pickSpecialVariety() {
        try {
            if (!hasPickedVariety && accessories.isEmpty()) {
                return
            }

            if (!hasPickedVariety && includeSpecial()) {
                pickAccessory()
            }

        } finally {
            hasPickedVariety = true
        }
    }
    protected fun pickSpecialAccessory() {
        try {
            if (!hasPickedAccessory && accessories.isEmpty()) {
                return
            }

            if (!hasPickedAccessory && includeSpecial()) {
                pickAccessory()
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
            .fold(Pair(GameRarity.COMMON, Double.POSITIVE_INFINITY)) {a, b -> if (a.second < b.second) a else b }
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

    abstract fun generateBox(): MysteryBox
}

class VarietyBoxAssembler(config: Config, catalogue: Catalogue, value: BigDecimal, excludeTrickTaker: Boolean = false)
    : MysteryBoxAssembler(config, catalogue, value) {

    init {
        hasPickedTrickTaker = excludeTrickTaker
    }

    fun pickSpecials() {
        pickSpecialMythic(varieties)
        pickSpecialTrickTaker()
        pickSpecialAccessory()
    }

    override fun generateBox(): MysteryBox {
        pickSpecials()

        when (pickNext()) {
            GameRarity.COMMON -> pickCommon(varieties)
            GameRarity.UNCOMMON -> pickUncommon(varieties)
            GameRarity.RARE, GameRarity.MYTHIC -> pickRare(varieties)
        }
        TODO("Not yet implemented")
    }
}