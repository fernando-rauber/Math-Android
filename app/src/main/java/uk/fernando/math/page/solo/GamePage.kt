package uk.fernando.math.page.solo

import android.media.MediaPlayer
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.inject
import uk.fernando.advertising.AdInterstitial
import uk.fernando.math.R
import uk.fernando.math.activity.MainActivity
import uk.fernando.math.component.MyAnimation
import uk.fernando.math.component.OnLifecycleEvent
import uk.fernando.math.component.game.MyCountDown
import uk.fernando.math.component.game.MyDialogResult
import uk.fernando.math.component.game.MyGameDialog
import uk.fernando.math.component.game.MyQuestionDisplay
import uk.fernando.math.datastore.PrefsStore
import uk.fernando.math.ext.playAudio
import uk.fernando.math.ext.timerFormat
import uk.fernando.math.navigation.Directions
import uk.fernando.math.viewmodel.solo.GameViewModel

@Composable
fun GamePage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: GameViewModel = getViewModel()
) {
    val coroutine = rememberCoroutineScope()
    val fullScreenAd = AdInterstitial(LocalContext.current as MainActivity, stringResource(R.string.ad_full_page))
    val soundCountDown = MediaPlayer.create(LocalContext.current, R.raw.sound_countdown)
    val soundCorrect = MediaPlayer.create(LocalContext.current, R.raw.sound_correct)
    val soundIncorrect = MediaPlayer.create(LocalContext.current, R.raw.sound_incorrect)
    val prefs: PrefsStore by inject()
    val isSoundEnable = prefs.soundEnable().collectAsState(initial = false)

    LaunchedEffect(Unit) {
        soundCountDown.playAudio(isSoundEnable.value)
        viewModel.createGame()
    }

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_STOP && !viewModel.isGameFinished.value)
            viewModel.isGamePaused.value = true
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
                        soundCorrect.playAudio(isSoundEnable.value)
                    else
                        soundIncorrect.playAudio(isSoundEnable.value)
                }
            }
        }

        // Dialogs
        MyCountDown { viewModel.startChronometer() }

        PauseResumeGame(viewModel, onExitGame = { navController.popBackStack() })

        MyDialogResult(
            viewModel = viewModel,
            fullScreenAd = fullScreenAd
        ) {
            coroutine.launch {
                navController.navigate("${Directions.summary.name}/${viewModel.getHistoryId()}") {
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
            text = "${viewModel.nextQuestionCounter.value}-${viewModel.maxQuestion}",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 26.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun QuestionAndAnswers(viewModel: GameViewModel, playSound: (Boolean?) -> Unit) {
    viewModel.currentQuestion.value?.let { question ->

        MyQuestionDisplay(
            question = question,
            multipleChoice = if (viewModel.isMultipleChoice()) question.getMultipleChoicePlayerOne() else null,
            onClick = { answer ->
                playSound(viewModel.registerAnswer(answer))
            }
        )
    }
}

@Composable
private fun PauseResumeGame(viewModel: GameViewModel, onExitGame: () -> Unit) {
    MyAnimation(viewModel.isGamePaused.value) {
        MyGameDialog(
            image = R.drawable.ic_coffee_break,
            message = R.string.resume_message,
            buttonText = R.string.resume_action,
            onExitGame = onExitGame,
            onClick = { viewModel.pauseUnpauseGame() }
        )
    }
}