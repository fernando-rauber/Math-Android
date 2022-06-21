package uk.fernando.math.component.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import uk.fernando.math.ui.theme.green_pastel
import uk.fernando.math.ui.theme.orange
import uk.fernando.math.ui.theme.pastel_red
import uk.fernando.math.ui.theme.purple

@Composable
fun ColumnScope.MyQuestionDisplay(question: QuestionEntity, multipleChoice: List<Int>?, isMultiplayer: Boolean = false, onClick: (Int) -> Unit) {
    Question(question, isMultiplayer)

    if (question.operator.isBooleanQuestion())
        BooleanChoice(isMultiplayer, onClick = onClick)
    else {
        if (multipleChoice != null && multipleChoice.isNotEmpty())
            MultipleChoice(multipleChoice, isMultiplayer, onClick = onClick)
        else
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
private fun ColumnScope.BooleanChoice(isMultiplayer: Boolean, onClick: (Int) -> Unit) {
    Column(Modifier.weight(if (isMultiplayer) 0.6f else 0.4f)) {
        Spacer(Modifier.weight(0.8f))
        Row(Modifier.weight(1f)) {
            AnswerCard(1, green_pastel, onClick, true)
            Spacer(Modifier.width(16.dp))
            AnswerCard(0, pastel_red, onClick, true)
        }
    }
}

@Composable
private fun ColumnScope.MultipleChoice(answerList: List<Int>, isMultiplayer: Boolean, onClick: (Int) -> Unit) {
    Column(Modifier.weight(if (isMultiplayer) 0.6f else 0.4f)) {
        Row(Modifier.weight(1f)) {
            AnswerCard(answerList[0], orange, onClick)
            Spacer(Modifier.width(16.dp))
            AnswerCard(answerList[1], green_pastel, onClick)
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.weight(1f)) {
            AnswerCard(answerList[2], pastel_red, onClick)
            Spacer(Modifier.width(16.dp))
            AnswerCard(answerList[3], purple, onClick)
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
                    onClick(textField.toInt())
                    textField = ""
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
                onClick(textField.toInt())
                textField = ""
            },
            text = stringResource(R.string.check_action)
        )
    }
}

@Composable
private fun RowScope.AnswerCard(answer: Int, color: Color, onClick: (Int) -> Unit, isBoolean: Boolean = false) {

    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clip(MaterialTheme.shapes.medium)
            .background(color)
            .clickable { onClick(answer) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isBoolean) stringResource(answer.toFalseTrue()) else "$answer",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
    }
}