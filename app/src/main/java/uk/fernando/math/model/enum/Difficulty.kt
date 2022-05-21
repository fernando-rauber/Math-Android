package uk.fernando.math.model.enum

enum class Difficulty(val value: Int) {
    EASY(1),
    MEDIUM(2),
    HARD(3);

    companion object {
        fun getByValue(value: Int) = values().firstOrNull { it.value == value }
    }
}