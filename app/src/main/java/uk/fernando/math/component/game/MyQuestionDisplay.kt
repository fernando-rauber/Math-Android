package uk.fernando.math.component.game

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.fernando.math.R
import uk.fernando.math.component.MyButton
import uk.fernando.math.component.MyTextField
import uk.fernando.math.database.entity.QuestionEntity
import uk.fernando.math.ext.isBooleanQuestion
import uk.fernando.math.ext.toFalseTrue
import uk.fernando.math.theme.game_green
import uk.fernando.math.theme.game_orange
import uk.fernando.math.theme.game_purple
import uk.fernando.math.theme.game_red

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MyQuestionDisplay(question: QuestionEntity, multipleChoice: List<Int>?, isMultiplayer: Boolean = false, onClick: (Int) -> Unit) {

    Column {

        Question(question, isMultiplayer)

        AnimatedContent(
            targetState = question,
            modifier = Modifier.weight(if (isMultiplayer) 0.6f else 0.4f),
            transitionSpec = {
                (slideInHorizontally { height -> height } + fadeIn() with
                        slideOutHorizontally { height -> -height } + fadeOut()).using(
                    SizeTransform(clip = false)
                )
            }
        ) { quest ->

            if (quest.operator.isBooleanQuestion())
                BooleanChoice(onClick = onClick)
            else {
                if (multipleChoice != null && multipleChoice.isNotEmpty())
                    MultipleChoice(multipleChoice, onClick = onClick)
            }
        }

        if (!question.operator.isBooleanQuestion() && multipleChoice == null)
            OpenAnswer(onClick = onClick)
    }
}

@Composable
private fun ColumnScope.Question(question: QuestionEntity, isMultiplayer: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(if (isMultiplayer) 0.3f else 0.6f),
        contentAlignment = Alignment.Center
    ) {

        MyQuestion(
            value1 = question.value1,
            value2 = question.value2,
            operator = question.operator,
            result = "?",
            size = if (isMultiplayer) 30 else 40
        )
    }
}

@Composable
private fun BooleanChoice(onClick: (Int) -> Unit) {
    Column {
        Spacer(Modifier.weight(0.8f))
        Row(Modifier.weight(1f)) {
            AnswerCard(1, game_green, onClick, true)
            Spacer(Modifier.width(16.dp))
            AnswerCard(0, game_red, onClick, true)
        }
    }
}

@Composable
private fun MultipleChoice(answerList: List<Int>, onClick: (Int) -> Unit) {
    Column {
        Row(Modifier.weight(1f)) {
            AnswerCard(answerList[0], game_orange, onClick)
            Spacer(Modifier.width(16.dp))
            AnswerCard(answerList[1], game_green, onClick)
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.weight(1f)) {
            AnswerCard(answerList[2], game_red, onClick)
            Spacer(Modifier.width(16.dp))
            AnswerCard(answerList[3], game_purple, onClick)
        }
    }
}

@Composable
private fun OpenAnswer(onClick: (Int) -> Unit) {
    var textField by remember { mutableStateOf("") }

    Column(Modifier.fillMaxWidth()) {

        MyTextField(
            value = textField,
            onValueChange = {
                textField = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (textField.isNotEmpty()) {
                        val userAnswer = textField.toIntOrNull()
                        if (userAnswer != null)
                            onClick(userAnswer)

                        textField = ""
                    }
                }
            )
        )

        MyButton(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 50.dp),
            enabled = textField.isNotEmpty(),
            onClick = {
                val userAnswer = textField.toIntOrNull()
                if (userAnswer != null)
                    onClick(userAnswer)

                textField = ""
            },
            text = stringResource(R.string.check_action)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RowScope.AnswerCard(answer: Int, color: Color, onClick: (Int) -> Unit, isBoolean: Boolean = false) {

    Card(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(color),
        shape = MaterialTheme.shapes.medium,
        onClick = { onClick(answer) }) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isBoolean) stringResource(answer.toFalseTrue()) else "$answer",
                fontSize = 25.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}