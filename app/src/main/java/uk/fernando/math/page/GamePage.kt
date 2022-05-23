package uk.fernando.math.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import uk.fernando.math.R
import uk.fernando.math.component.MyButton
import uk.fernando.math.component.MyTextField
import uk.fernando.math.ext.mathOperator
import uk.fernando.math.ext.safeNav
import uk.fernando.math.ext.timerFormat
import uk.fernando.math.model.Question
import uk.fernando.math.navigation.Directions
import uk.fernando.math.ui.theme.*
import uk.fernando.math.viewmodel.GameViewModel
import kotlin.time.Duration.Companion.seconds

@ExperimentalMaterialApi
@Composable
fun GamePage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: GameViewModel = getViewModel()
) {
    val coroutine = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.startGame()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Timer
        Timer(viewModel.chronometerSeconds.value)

        viewModel.currentQuestion.value?.let { question ->

            Question(question)

            if (question.multipleChoices != null) {
                MultipleChoice(
                    correctAnswer = question.answer,
                    answerList = question.multipleChoices,
                    onClick = { answer -> viewModel.checkAnswer(answer) }
                )
            } else {
                OpenAnswer(correctAnswer = question.answer)
            }

        }

        if (viewModel.historyId.value != 0)
            MyButton(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 50.dp),
                onClick = {
                    coroutine.launch {
                        navController.safeNav("${Directions.summary.name}/${viewModel.historyId.value}")
                    }
                },
                text = "Result"
            )
    }

}

@Composable
private fun Timer(seconds: Int) {

    Row() {
        Icon(
            painter = painterResource(id = R.drawable.ic_timer),
            contentDescription = null,
        )
        Text(
            text = seconds.timerFormat(),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )

    }
}

@Composable
private fun ColumnScope.Question(question: Question) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "${question.first} ${question.operator.mathOperator()} ${question.second} = ?",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )

    }

}

@Composable
private fun MultipleChoice(correctAnswer: Int, answerList: List<Int>, onClick: (Int) -> Unit) {
    Column {
        Row {
            AnswerCard(correctAnswer, answerList[0], orange, onClick)
            Spacer(modifier = Modifier.width(16.dp))
            AnswerCard(correctAnswer, answerList[1], pastel_red, onClick)
        }
        Row(Modifier.padding(top = 16.dp)) {
            AnswerCard(correctAnswer, answerList[2], green_pastel, onClick)
            Spacer(modifier = Modifier.width(16.dp))
            AnswerCard(correctAnswer, answerList[3], purple, onClick)
        }
    }
}

@Composable
private fun OpenAnswer(correctAnswer: Int) {
    var textField by remember { mutableStateOf("") }

    Column(Modifier.fillMaxWidth()) {

        MyTextField(
            value = textField,
            onValueChange = { textField = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { }
            )
        )

        MyButton(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 50.dp),
            onClick = { }, text = "Check"
        )
    }

}

@Composable
private fun RowScope.AnswerCard(correctAnswer: Int, answer: Int, color: Color, onClick: (Int) -> Unit) {
    var currentColor by remember { mutableStateOf(color) }

    Box(
        modifier = Modifier
            .weight(1f)
            .defaultMinSize(minHeight = 100.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(currentColor)
            .clickable {
                onClick(answer)
                if (correctAnswer != answer)
                    currentColor = red
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$answer",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
    }
}