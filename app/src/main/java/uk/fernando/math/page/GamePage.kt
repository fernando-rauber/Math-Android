package uk.fernando.math.page

import android.media.MediaPlayer
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.inject
import uk.fernando.advertising.AdInterstitial
import uk.fernando.advertising.enum.AdState
import uk.fernando.math.R
import uk.fernando.math.activity.MainActivity
import uk.fernando.math.component.MyButton
import uk.fernando.math.component.MyDialog
import uk.fernando.math.component.MyTextField
import uk.fernando.math.datastore.PrefsStore
import uk.fernando.math.ext.mathOperator
import uk.fernando.math.ext.playAudio
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
    val fullScreenAd = AdInterstitial(LocalContext.current as MainActivity, stringResource(R.string.ad_full_page))
    val soundCorrect = MediaPlayer.create(LocalContext.current, R.raw.sound_correct)
    val soundIncorrect = MediaPlayer.create(LocalContext.current, R.raw.sound_incorrect)

    LaunchedEffect(Unit) {
        viewModel.createGame()
    }

    Box {

        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Timer(viewModel)

            QuestionDisplay(
                viewModel = viewModel,
                playAudio = { isCorrectAnswer ->
                    isCorrectAnswer?.let {
                        if (isCorrectAnswer)
                            soundCorrect.playAudio()
                        else
                            soundIncorrect.playAudio()
                    }
                }
            )
        }

        CountDownStart(viewModel)

        ResumeGame(viewModel)

        DialogResult(
            navController = navController,
            viewModel = viewModel,
            fullScreenAd = fullScreenAd
        )
    }
}


@Composable
private fun DialogResult(navController: NavController, viewModel: GameViewModel, fullScreenAd: AdInterstitial) {
    val coroutine = rememberCoroutineScope()
    val dataStore: PrefsStore by inject()
    val isPremium = dataStore.isPremium().collectAsState(true)

    AnimatedVisibility(visible = viewModel.historyId.value != 0) {
        CustomDialog(
            image = R.drawable.fireworks,
            message = R.string.result_message,
            buttonText = R.string.result_action
        ) {
            coroutine.launch {
                if (isPremium.value) {
                    navigateToResult(navController, "${viewModel.historyId.value}")
                } else {
                    fullScreenAd.showAdvert().collect { state ->
                        if (state == AdState.DISMISSED || state == AdState.FAIL)
                            navigateToResult(navController, "${viewModel.historyId.value}")
                    }
                }
            }
        }
    }
}

private fun navigateToResult(navController: NavController, historyId: String) {
    navController.navigate("${Directions.summary.name}/$historyId") {
        popUpTo(Directions.game.name) { inclusive = true }
        popUpTo(Directions.createGame.name) { inclusive = true }
    }
}

@Composable
private fun Timer(viewModel: GameViewModel) {
    Box {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(36.dp),
                painter = painterResource(id = R.drawable.ic_timer),
                contentDescription = null,
            )
            Text(
                modifier = Modifier.padding(start = 2.dp),
                text = viewModel.chronometerSeconds.value.timerFormat(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { viewModel.pauseUnpauseGame() }) {
                Icon(painterResource(id = R.drawable.ic_pause), "pause")
            }
        }

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "${viewModel.nextQuestion.value}-${viewModel.maxQuestion}",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 26.sp
        )
    }
}

@Composable
private fun ColumnScope.QuestionDisplay(viewModel: GameViewModel, playAudio: (Boolean?) -> Unit) {
    viewModel.currentQuestion.value?.let { question ->

        Question(question)

        if (question.multipleChoices != null) {
            MultipleChoice(
                correctAnswer = question.answer,
                answerList = question.multipleChoices,
                onClick = { answer -> playAudio(viewModel.checkAnswer(answer)) }
            )
        } else {
            OpenAnswer(correctAnswer = question.answer,
                onClick = { answer -> playAudio(viewModel.checkAnswer(answer)) }
            )
        }
    }
}


@Composable
private fun CountDownStart(viewModel: GameViewModel) {
    var countDown by remember { mutableStateOf(3) }

    LaunchedEffect(Unit) {
        while (countDown >= 0) {
            delay(1.seconds)
            countDown--
            if (countDown == -1)
                viewModel.startChronometer()
        }
    }

    AnimatedVisibility(
        visible = countDown > 0,
        exit = fadeOut()
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.6f))
        ) {

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "$countDown",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 200.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ResumeGame(viewModel: GameViewModel) {
    AnimatedVisibility(viewModel.isGamePaused.value) {
        CustomDialog(
            image = R.drawable.coffee_break,
            message = R.string.resume_message,
            buttonText = R.string.resume_action,
            onClick = { viewModel.pauseUnpauseGame() }
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
    val color1 = remember { mutableStateOf(orange) }
    val color2 = remember { mutableStateOf(green_pastel) }
    val color3 = remember { mutableStateOf(pastel_red) }
    val color4 = remember { mutableStateOf(purple) }

    // To refresh all the multiple choices back to the original color
    if (correctAnswer > 0) {
        color1.value = orange
        color2.value = green_pastel
        color3.value = pastel_red
        color4.value = purple
    }

    Column {
        Row {
            AnswerCard(correctAnswer, answerList[0], color1, onClick)
            Spacer(Modifier.width(16.dp))
            AnswerCard(correctAnswer, answerList[1], color2, onClick)
        }
        Row(Modifier.padding(top = 16.dp)) {
            AnswerCard(correctAnswer, answerList[2], color3, onClick)
            Spacer(Modifier.width(16.dp))
            AnswerCard(correctAnswer, answerList[3], color4, onClick)
        }
    }
}

@Composable
private fun OpenAnswer(correctAnswer: Int, onClick: (Int) -> Unit) {
    var textField by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxWidth()) {

        MyTextField(
            value = textField,
            onValueChange = {
                textField = it
                isError = false
            },
            trailingIcon = { if (isError) Icon(Icons.Filled.Warning, "error", tint = MaterialTheme.colorScheme.error) },
            errorText = stringResource(R.string.wrong_answer_message),
            isError = isError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onClick(textField.toInt())
                    if (textField.toInt() != correctAnswer) {
                        isError = true
                    } else {
                        isError = false
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
                onClick(textField.toInt())
                if (textField.toInt() != correctAnswer) {
                    isError = true
                } else {
                    isError = false
                    textField = ""
                }
            },
            text = stringResource(R.string.check_action)
        )
    }

}

@Composable
private fun RowScope.AnswerCard(correctAnswer: Int, answer: Int, color: MutableState<Color>, onClick: (Int) -> Unit) {

    Box(
        modifier = Modifier
            .weight(1f)
            .defaultMinSize(minHeight = 100.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(color.value)
            .clickable {
                onClick(answer)
                if (correctAnswer != answer) {
                    color.value = red
                }
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

@Composable
private fun CustomDialog(
    @DrawableRes image: Int,
    @StringRes message: Int,
    @StringRes buttonText: Int,
    onClick: () -> Unit
) {
    MyDialog {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                modifier = Modifier.padding(vertical = 30.dp),
                painter = painterResource(id = image),
                contentDescription = null
            )

            Text(
                modifier = Modifier.padding(vertical = 15.dp),
                text = stringResource(message),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 25.sp,
                letterSpacing = 0.30.sp
            )

            MyButton(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 50.dp),
                onClick = onClick,
                text = stringResource(buttonText)
            )
        }
    }
}