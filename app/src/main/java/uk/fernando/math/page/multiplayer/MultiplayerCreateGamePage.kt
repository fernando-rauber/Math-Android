package uk.fernando.math.page.multiplayer

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import uk.fernando.math.R
import uk.fernando.math.component.MyBackground
import uk.fernando.math.component.MyButton
import uk.fernando.math.component.MyTextField
import uk.fernando.math.component.TopNavigationBar
import uk.fernando.math.ext.safeNav
import uk.fernando.math.navigation.Directions
import uk.fernando.math.page.BasicMathOptions
import uk.fernando.math.page.Difficulty
import uk.fernando.math.page.QuestionQuantity
import uk.fernando.math.viewmodel.MultiplayerCreateGameViewModel

@ExperimentalMaterialApi
@Composable
fun MultiplayerCreateGamePage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: MultiplayerCreateGameViewModel = getViewModel()
) {
    val coroutine = rememberCoroutineScope()

    MyBackground {

        Column(Modifier.fillMaxSize()) {

            TopNavigationBar(
                title = R.string.question_creation_title,
                leftIcon = R.drawable.ic_arrow_back,
                onLeftIconClick = { navController.popBackStack() }
            )

            Surface(
                modifier = Modifier.padding(16.dp),
                shadowElevation = 7.dp,
                shape = MaterialTheme.shapes.medium
            ) {

                Box(Modifier.padding(16.dp)) {

                    Column(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())) {

                        PlayerName(
                            playerName = "Player 1",
                            label = R.string.player_one,
                            onValueChange = viewModel::setPlayer1
                        )

                        PlayerName(
                            playerName = "Player 2",
                            label = R.string.player_two,
                            onValueChange = viewModel::setPlayer1
                        )

                        Divider(Modifier.padding(vertical = 10.dp))

                        BasicMathOptions {
                            viewModel.setMathOptions(it)
                        }

                        Divider(Modifier.padding(vertical = 10.dp))

                        QuestionQuantity { quantity ->
                            viewModel.setQuantity(quantity)
                        }

                        Divider(Modifier.padding(vertical = 10.dp))

                        Difficulty { difficult ->
                            viewModel.setDifficulty(difficult)
                        }

                        Spacer(Modifier.height(60.dp))
                    }

                    MyButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .defaultMinSize(minHeight = 50.dp),
                        text = stringResource(id = R.string.start_action),
                        isLoading = viewModel.loading.value,
                        onClick = {
                            coroutine.launch {
                                viewModel.generateQuestion().collect {
                                    if (it) navController.safeNav(Directions.multiplayerGame.name)
                                }
                            }
                        }
                    )
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
                onValueChange(it)
                player = it
            },
        )
    }
}