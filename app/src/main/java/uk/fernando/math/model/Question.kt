package uk.fernando.math.model

data class Question(
    val first: String,
    val operator: Int,
    val second: String,
    val answer: Int,
    val multipleChoices: List<Int>? = null
){
    override fun toString(): String {
        return "$first $operator $second = $answer  --- ${multipleChoices.toString()}"
    }
}
