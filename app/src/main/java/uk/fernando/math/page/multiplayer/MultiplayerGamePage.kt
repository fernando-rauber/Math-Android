package uk.fernando.math.page.multiplayer

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import uk.fernando.math.datastore.PrefsStore
import uk.fernando.math.ext.noRippleClickable
import uk.fernando.math.ext.playAudio
import uk.fernando.math.navigation.Directions
import uk.fernando.math.page.solo.CountDownStart
import uk.fernando.math.page.solo.CustomDialog
import uk.fernando.math.page.solo.QuestionDisplay
import uk.fernando.math.ui.theme.orange
import uk.fernando.math.ui.theme.star_red
import uk.fernando.math.viewmodel.multiplayer.MultiplayerGameViewModel
import kotlin.time.Duration.Companion.seconds

@ExperimentalMaterialApi
@Composable
fun MultiplayerGamePage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: MultiplayerGameViewModel = getViewModel()
) {
    val fullScreenAd = AdInterstitial(LocalContext.current as MainActivity, stringResource(R.string.ad_full_page))
    val soundCorrect = MediaPlayer.create(LocalContext.current, R.raw.sound_correct)
    val soundIncorrect = MediaPlayer.create(LocalContext.current, R.raw.sound_incorrect)

    LaunchedEffect(Unit) {
        viewModel.createGame()
    }

    Box {
        Column(Modifier.fillMaxSize()) {

            Player2Screen(
                viewModel = viewModel,
                playSound = { isCorrectAnswer ->
                    isCorrectAnswer?.let {
                        if (isCorrectAnswer)
                            soundCorrect.playAudio()
                        else
                            soundIncorrect.playAudio()
                    }
                }
            )

            Settings(viewModel)

            Player1Screen(
                viewModel = viewModel,
                playSound = { isCorrectAnswer ->
                    isCorrectAnswer?.let {
                        if (isCorrectAnswer)
                            soundCorrect.playAudio()
                        else
                            soundIncorrect.playAudio()
                    }
                }
            )
        }

        CountDownStart { }

        // Dialogs
        PauseResumeGame(viewModel, onExitGame = { navController.popBackStack() })

        DialogResult(
            navController = navController,
            viewModel = viewModel,
            fullScreenAd = fullScreenAd
        )
    }
}

@Composable
private fun PauseResumeGame(viewModel: MultiplayerGameViewModel, onExitGame: () -> Unit) {
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
private fun ColumnScope.Player1Screen(viewModel: MultiplayerGameViewModel, playSound: (Boolean?) -> Unit) {
    Box(Modifier.weight(1f)) {

        // Question & Multiple choice
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            viewModel.currentQuestion.value?.let { question ->

                QuestionDisplay(question = question) { answer ->
                    playSound(viewModel.registerAnswerPlayer1(answer))
                }
            }
        }

        MyAnimation(viewModel.player2Waiting.value) {
            QuestionCountDown {
                viewModel.registerAnswerPlayer1(null)
            }
        }

        // Block screen until other player choose their answer
        MyAnimation(viewModel.player1Waiting.value) { AwaitingPlayer() }

    }
}

@Composable
private fun ColumnScope.Player2Screen(viewModel: MultiplayerGameViewModel, playSound: (Boolean?) -> Unit) {
    Box(
        Modifier
            .weight(1f)
            .rotate(180f)
    ) {

        // Question & Multiple choice
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            viewModel.currentQuestion.value?.let { question ->

                QuestionDisplay(question = question) { answer ->
                    playSound(viewModel.registerAnswerPlayer2(answer))
                }
            }
        }

        MyAnimation(viewModel.player1Waiting.value) {
            QuestionCountDown {
                viewModel.registerAnswerPlayer2(null)
            }
        }

        // Block screen until other player choose their answer
        MyAnimation(viewModel.player2Waiting.value) { AwaitingPlayer() }
    }
}

@Composable
private fun DialogResult(navController: NavController, viewModel: MultiplayerGameViewModel, fullScreenAd: AdInterstitial) {
    val coroutine = rememberCoroutineScope()
    val dataStore: PrefsStore by inject()
    val isPremium = dataStore.isPremium().collectAsState(true)

    MyAnimation(visible = viewModel.historyId.value != 0) {
        if (!isPremium.value)
            fullScreenAd.showAdvert()

        CustomDialog(
            image = R.drawable.fireworks,
            message = R.string.result_message,
            buttonText = R.string.result_action
        ) {
            coroutine.launch {
                navController.navigate("${Directions.multiplayerSummary.name}/${viewModel.historyId.value}") {
                    popUpTo(Directions.multiplayerGame.name) { inclusive = true }
                    popUpTo(Directions.multiplayerCreateGame.name) { inclusive = true }
                }
            }
        }
    }
}

@Composable
private fun Settings(viewModel: MultiplayerGameViewModel) {
    Row(
        modifier = Modifier
            .background(orange)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Text(
            text = "${viewModel.nextQuestion.value}-${viewModel.maxQuestion}",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 26.sp,
            color = MaterialTheme.colorScheme.onBackground
        )


        IconButton(onClick = { viewModel.pauseUnpauseGame() }) {
            Icon(
                painterResource(id = R.drawable.ic_pause),
                contentDescription = "pause",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Text(
            modifier = Modifier.rotate(180f),
            text = "${viewModel.nextQuestion.value}-${viewModel.maxQuestion}",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 26.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun QuestionCountDown(onFinish: () -> Unit) {
    var countDown by remember { mutableStateOf(10) }

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
            .background(star_red.copy(0.6f))
    ) {

        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 10.dp),
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
            .background(Color.Black.copy(0.6f))
            .noRippleClickable { }) {

        Text(
            text = stringResource(id = R.string.waiting_player),
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}