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
import uk.fernando.math.R
import uk.fernando.math.activity.MainActivity
import uk.fernando.math.component.MyAnimation
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

            QuestionAndAnswers(viewModel) { isCorrectAnswer ->
                isCorrectAnswer?.let {
                    if (isCorrectAnswer)
                        soundCorrect.playAudio()
                    else
                        soundIncorrect.playAudio()
                }
            }
        }

        // Dialogs
        CountDownStart { viewModel.startChronometer() }

        PauseResumeGame(viewModel, onExitGame = { navController.popBackStack() })

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

    MyAnimation(viewModel.historyId.value != 0) {
        if (!isPremium.value)
            fullScreenAd.showAdvert()

        CustomDialog(
            image = R.drawable.fireworks,
            message = R.string.result_message,
            buttonText = R.string.result_action
        ) {
            coroutine.launch {
                navController.navigate("${Directions.summary.name}/${viewModel.historyId.value}") {
                    popUpTo(Directions.game.name) { inclusive = true }
                    popUpTo(Directions.createGame.name) { inclusive = true }
                }
            }
        }
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
                tint = MaterialTheme.colorScheme.onBackground
            )
            Text(
                modifier = Modifier.padding(start = 2.dp),
                text = viewModel.chronometerSeconds.value.timerFormat(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { viewModel.pauseUnpauseGame() }) {
                Icon(
                    painterResource(id = R.drawable.ic_pause),
                    contentDescription = "pause",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "${viewModel.nextQuestion.value}-${viewModel.maxQuestion}",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 26.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun ColumnScope.QuestionAndAnswers(viewModel: GameViewModel, playSound: (Boolean?) -> Unit) {
    viewModel.currentQuestion.value?.let { question ->

        QuestionDisplay(
            question = question,
            onClick = { answer ->
                playSound(viewModel.registerAnswer(answer))
            }
        )
    }
}

@Composable
fun ColumnScope.QuestionDisplay(question: Question, onClick: (Int) -> Unit) {
    Question(question)

    if (question.multipleChoices != null)
        MultipleChoice(question.multipleChoices.shuffled(), onClick = onClick)
    else
        OpenAnswer(onClick = onClick)
}

@Composable
fun CountDownStart(onStart: () -> Unit) {
    var countDown by remember { mutableStateOf(3) }

    LaunchedEffect(Unit) {
        while (countDown >= 0) {
            delay(1.seconds)
            countDown--
            if (countDown == -1)
                onStart()
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
private fun PauseResumeGame(viewModel: GameViewModel, onExitGame: () -> Unit) {
    MyAnimation(viewModel.isGamePaused.value) {
        CustomDialog(
            image = R.drawable.coffee_break,
            message = R.string.resume_message,
            buttonText = R.string.resume_action,
            onExitGame = onExitGame,
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
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun MultipleChoice(answerList: List<Int>, onClick: (Int) -> Unit) {
    Column {
        Row {
            AnswerCard(answerList[0], orange, onClick)
            Spacer(Modifier.width(16.dp))
            AnswerCard(answerList[1], green_pastel, onClick)
        }
        Row(Modifier.padding(top = 16.dp)) {
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
private fun RowScope.AnswerCard(answer: Int, color: Color, onClick: (Int) -> Unit) {

    Box(
        modifier = Modifier
            .weight(1f)
            .defaultMinSize(minHeight = 100.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(color)
            .clickable { onClick(answer) },
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
fun CustomDialog(
    @DrawableRes image: Int,
    @StringRes message: Int,
    @StringRes buttonText: Int,
    onExitGame: (() -> Unit)? = null,
    onClick: () -> Unit,
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

            if (onExitGame != null)
                MyButton(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 50.dp),
                    onClick = onExitGame,
                    color = star_red,
                    text = stringResource(R.string.exit_game_action)
                )
        }
    }
}