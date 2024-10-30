/**
 * The type of game to be added to the mystery box
 */
enum class GameCategory {
    /**
     * The game's a trick-taker.
     */
    TRICK_TAKER,

    /**
     * The game's a variety game (not a trick-taker).
     */
    VARIETY
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