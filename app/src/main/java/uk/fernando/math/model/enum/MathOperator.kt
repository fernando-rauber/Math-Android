package uk.fernando.math.model.enum

enum class MathOperator(val value: Int) {
    ADDITION(1),
    SUBTRACTION(2),
    DIVISION(3),
    MULTIPLICATION(4),
    PERCENTAGE(5),
    SQUARE(6),
    GREATER_THAN(7),
    LESSER_THAN(8);

    companion object {
        fun getByValue(value: Int) = values().firstOrNull { it.value == value }
    }
}