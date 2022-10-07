package uk.fernando.math.page.multiplayer

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.inject
import uk.fernando.advertising.AdInterstitial
import uk.fernando.math.R
import uk.fernando.math.activity.MainActivity
import uk.fernando.math.component.game.MyCountDown
import uk.fernando.math.component.game.MyDialogResult
import uk.fernando.math.component.game.MyGameDialog
import uk.fernando.math.component.game.MyQuestionDisplay
import uk.fernando.math.datastore.PrefsStore
import uk.fernando.math.navigation.Directions
import uk.fernando.math.theme.game_orange
import uk.fernando.math.theme.red
import uk.fernando.math.viewmodel.multiplayer.MultiplayerGameViewModel
import uk.fernando.util.component.MyAnimatedVisibility
import uk.fernando.util.component.MyIconButton
import uk.fernando.util.ext.clickableSingle
import uk.fernando.util.ext.playAudio
import kotlin.time.Duration.Companion.seconds

@Composable
fun MultiplayerGamePage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: MultiplayerGameViewModel = getViewModel()
) {
    val coroutine = rememberCoroutineScope()
    val fullScreenAd = AdInterstitial(LocalContext.current as MainActivity, stringResource(R.string.ad_full_page))
    val soundCountDown = MediaPlayer.create(LocalContext.current, R.raw.sound_countdown)
    val soundCorrect = MediaPlayer.create(LocalContext.current, R.raw.sound_correct)
    val soundIncorrect = MediaPlayer.create(LocalContext.current, R.raw.sound_incorrect)
    val prefs: PrefsStore by inject()
    val isSoundEnable = prefs.isSoundEnabled().collectAsState(initial = true)

    LaunchedEffect(Unit) {
        soundCountDown.playAudio(isSoundEnable.value)
        viewModel.createGame()
    }

    Box {
        Column(Modifier.fillMaxSize()) {

            Box(
                Modifier
                    .weight(1f)
                    .rotate(180f)
            ) {
                Player2Screen(
                    viewModel = viewModel,
                    playSound = { isCorrectAnswer ->
                        isCorrectAnswer?.let {
                            if (isCorrectAnswer)
                                soundCorrect.playAudio(isSoundEnable.value)
                            else
                                soundIncorrect.playAudio(isSoundEnable.value)
                        }
                    }
                )

                MyCountDown(startSoundEffect = {
                    coroutine.launch(Dispatchers.IO) {
                        soundCountDown.playAudio(isSoundEnable.value)
                    }
                }) {}
            }

            Settings(viewModel)

            Box(Modifier.weight(1f)) {

                Player1Screen(
                    viewModel = viewModel,
                    playSound = { isCorrectAnswer ->
                        isCorrectAnswer?.let {
                            if (isCorrectAnswer)
                                soundCorrect.playAudio(isSoundEnable.value)
                            else
                                soundIncorrect.playAudio(isSoundEnable.value)
                        }
                    }
                )

                MyCountDown({}) { }
            }
        }

        // Dialogs
        PauseResumeGame(viewModel, onExitGame = { navController.popBackStack() })

        MyDialogResult(
            viewModel = viewModel,
            fullScreenAd = fullScreenAd
        ) {
            coroutine.launch {
                navController.navigate("${Directions.multiplayerSummary.name}/${viewModel.getHistoryId()}") {
                    popUpTo(Directions.multiplayerGame.name) { inclusive = true }
                    popUpTo(Directions.multiplayerCreateGame.name) { inclusive = true }
                }
            }
        }
    }
}

@Composable
private fun PauseResumeGame(viewModel: MultiplayerGameViewModel, onExitGame: () -> Unit) {
    MyAnimatedVisibility(viewModel.isGamePaused.value) {
        MyGameDialog(
            image = R.drawable.ic_coffee_break,
            message = R.string.resume_message,
            buttonText = R.string.resume_action,
            onExitGame = onExitGame,
            onClick = { viewModel.pauseUnpauseGame() }
        )
    }
}

@Composable
private fun Player1Screen(viewModel: MultiplayerGameViewModel, playSound: (Boolean?) -> Unit) {
    Box {

        // Question & Multiple choice
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                modifier = Modifier.align(CenterHorizontally),
                text = viewModel.player1Name.value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            viewModel.currentQuestion.value?.let { question ->

                MyQuestionDisplay(
                    question = question,
                    isMultiplayer = true,
                    multipleChoice = question.getMultipleChoicePlayerOne()
                ) { answer ->
                    playSound(viewModel.registerAnswerPlayer1(answer))
                }
            }
        }

        MyAnimatedVisibility(viewModel.player2Waiting.value) {
            QuestionCountDown {
                viewModel.registerAnswerPlayer1(null)
            }
        }

        // Block screen until other player choose their answer
        MyAnimatedVisibility(viewModel.player1Waiting.value) { AwaitingPlayer() }

    }
}

@Composable
private fun Player2Screen(viewModel: MultiplayerGameViewModel, playSound: (Boolean?) -> Unit) {
    Box {
        // Question & Multiple choice
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                modifier = Modifier.align(CenterHorizontally),
                text = viewModel.player2Name.value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            viewModel.currentQuestion.value?.let { question ->

                MyQuestionDisplay(
                    question = question,
                    isMultiplayer = true,
                    multipleChoice = question.getMultipleChoicePlayerTwo()
                ) { answer ->
                    playSound(viewModel.registerAnswerPlayer2(answer))
                }
            }
        }

        MyAnimatedVisibility(viewModel.player1Waiting.value) {
            QuestionCountDown {
                viewModel.registerAnswerPlayer2(null)
            }
        }

        // Block screen until other player choose their answer
        MyAnimatedVisibility(viewModel.player2Waiting.value) { AwaitingPlayer() }
    }
}

@Composable
private fun Settings(viewModel: MultiplayerGameViewModel) {
    Row(
        modifier = Modifier
            .background(game_orange)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Text(
            text = "${viewModel.nextQuestionCounter.value}-${viewModel.maxQuestion}",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 26.sp,
            color = Color.White
        )

        MyIconButton(
            icon = R.drawable.ic_pause,
            onClick = { viewModel.pauseUnpauseGame() },
            tint = Color.White
        )

        Text(
            modifier = Modifier.rotate(180f),
            text = "${viewModel.nextQuestionCounter.value}-${viewModel.maxQuestion}",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 26.sp,
            color = Color.White
        )
    }
}

@Composable
private fun QuestionCountDown(onFinish: () -> Unit) {
    var countDown by remember { mutableStateOf(5) }

    LaunchedEffect(Unit) {
        while (countDown >= 0) {
            delay(1.seconds)
            countDown--
            if (countDown == 0)
                onFinish()
        }
    }

    Box(
        Modifier
            .fillMaxWidth()
            .background(red.copy(0.6f))
    ) {

        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 4.dp),
            text = "$countDown",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 35.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun AwaitingPlayer() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.7f))
            .clickableSingle(enabled = false) { }) {

        Text(
            text = stringResource(id = R.string.waiting_player),
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
    }
}