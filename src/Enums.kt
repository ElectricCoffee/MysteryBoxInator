import errors.UnknownCategoryException

/**
 * The type of game to be added to the mystery box
 */
enum class GameCategory {
    ACCESSORY,
    /**
     * The game's a trick-taker.
     */
    TRICK_TAKER,

    /**
     * The game's a variety game (not a trick-taker).
     */
    VARIETY;

    companion object {
        fun fromString(string: String): GameCategory {
            return when (string.lowercase()[0]) {
                'a' -> ACCESSORY
                't' -> TRICK_TAKER
                'v' -> VARIETY
                else -> throw UnknownCategoryException(string)
            }
        }
    }
}

/**
 * The status for adding a game to the mystery box
 */
enum class ItemAddStatus {
    /**
     * Adding was successful!
     */
    SUCCESS,

    /**
     * The adding was successful, but it's a special case (a rare game or a trick-taker)
     */
    SUCCESS_SPECIAL,

    /**
     * We tried to add the game, but its value would exceed the mystery box' target total.
     */
    EXCEEDS_TARGET_VALUE
}

enum class GameRarity(val value: Int) {
    /**
     * Ok Game (e.g. cat poker)
     */
    COMMON(1),

    /**
     * Good Game/Excellent Easy to get game (e.g. Gummi Trick)
     */
    UNCOMMON(2),

    /**
     * Hard to Get/Well liked Designer/Limited Print Run/Availability (e.g. Eleven)
     */
    RARE(3),

    /**
     * The games people try (and fail to) bribe me over,
     * request incessantly for in their mystery box,
     * and go generally bananas for and I probably have 2-3 copies
     * (e.g. Of what's left, Kbernsitch), or a signed copy
     */
    MYTHIC(4);

    companion object {
        fun fromInt(int: Int): GameRarity {
            return entries.first { it.value == int }
        }
    }
}