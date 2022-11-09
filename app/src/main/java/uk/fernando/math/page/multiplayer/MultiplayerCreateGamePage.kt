package uk.fernando.math.page.multiplayer

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.inject
import uk.fernando.math.R
import uk.fernando.math.component.MyBackground
import uk.fernando.math.component.MyTextField
import uk.fernando.math.component.TopNavigationBar
import uk.fernando.math.component.creation.MyDifficulty
import uk.fernando.math.component.creation.MyMathOperatorOptions
import uk.fernando.math.component.creation.MyQuestionQuantity
import uk.fernando.math.datastore.GamePrefsStore
import uk.fernando.math.datastore.PrefsStore
import uk.fernando.math.navigation.Directions
import uk.fernando.math.theme.orange
import uk.fernando.math.viewmodel.multiplayer.MultiplayerCreateGameViewModel
import uk.fernando.util.component.MyButton
import uk.fernando.util.ext.safeNav

@Composable
fun MultiplayerCreateGamePage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: MultiplayerCreateGameViewModel = getViewModel()
) {
    val coroutine = rememberCoroutineScope()
    val prefs: PrefsStore by inject()
    val isPremiumUser = prefs.isPremium().collectAsState(initial = false)

    val gamePrefs: GamePrefsStore by inject()
    val quantity = gamePrefs.quantity().collectAsState(initial = 10)
    val difficulty = gamePrefs.difficulty().collectAsState(initial = 1)
    val operators = gamePrefs.getOperators().collectAsState(initial = listOf(1, 2, 3, 4))

    MyBackground {

        Column(Modifier.fillMaxSize()) {

            TopNavigationBar(
                title = R.string.question_creation_title,
                onLeftIconClick = { navController.popBackStack() }
            )

            Surface(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                shadowElevation = 7.dp,
                shape = MaterialTheme.shapes.medium
            ) {

                Box(Modifier.padding(16.dp)) {

                    Column(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {

                        PlayerName(
                            playerName = "Player 1",
                            label = R.string.player_one,
                            onValueChange = viewModel::setPlayer1
                        )

                        PlayerName(
                            playerName = "Player 2",
                            label = R.string.player_two,
                            onValueChange = viewModel::setPlayer2
                        )

                        Divider(Modifier.padding(vertical = 10.dp))

                        MyMathOperatorOptions(operators.value, isPremiumUser.value) {
                            viewModel.setMathOptions(it)
                        }

                        Divider(Modifier.padding(vertical = 10.dp))

                        MyQuestionQuantity(quantity.value) { quantity ->
                            viewModel.setQuantity(quantity)
                        }

                        Divider(Modifier.padding(vertical = 10.dp))

                        MyDifficulty(difficulty.value) { difficult ->
                            viewModel.setDifficulty(difficult)
                        }

                        Spacer(Modifier.height(60.dp))
                    }

                    CreateGameButton(viewModel) {
                        coroutine.launch {
                            viewModel.generateQuestion().collect {
                                if (it) navController.safeNav(Directions.multiplayerGame.name)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayerName(playerName: String, @StringRes label: Int, onValueChange: (String) -> Unit) {
    var player by remember { mutableStateOf(playerName) }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        Text(
            modifier = Modifier.padding(start = 10.dp, bottom = 3.dp),
            style = MaterialTheme.typography.labelMedium,
            text = stringResource(label)
        )
        MyTextField(
            modifier = Modifier.fillMaxWidth(),
            value = player,
            onValueChange = {
                if (it.length <= 20) {
                    onValueChange(it)
                    player = it
                }
            }
        )
    }
}

@Composable
private fun BoxScope.CreateGameButton(viewModel: MultiplayerCreateGameViewModel, onClick: () -> Unit) {
    MyButton(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .defaultMinSize(minHeight = 50.dp),
        text = stringResource(id = R.string.start_action),
        isLoading = viewModel.loading.value,
        enabled = viewModel.isGameValid.value,
        color = orange,
        shape = ButtonDefaults.shape,
        onClick = onClick
    )
}