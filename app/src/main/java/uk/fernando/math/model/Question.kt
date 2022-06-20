package uk.fernando.math.model

data class Question(
    val value1: String,
    val operator: Int,
    val value2: String,
    val answer: Int,
    val multipleChoices: List<Int>? = null
){
    override fun toString(): String {
        return "$value1 $operator $value2 = $answer  --- ${multipleChoices.toString()}"
    }
}
