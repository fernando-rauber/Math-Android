package uk.fernando.math.page.solo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.inject
import uk.fernando.math.R
import uk.fernando.math.component.MyBackground
import uk.fernando.math.component.MyButton
import uk.fernando.math.component.TopNavigationBar
import uk.fernando.math.component.creation.MyDifficulty
import uk.fernando.math.component.creation.MyMathOperatorOptions
import uk.fernando.math.component.creation.MyQuestionQuantity
import uk.fernando.math.datastore.GamePrefsStore
import uk.fernando.math.datastore.PrefsStore
import uk.fernando.math.ext.safeNav
import uk.fernando.math.navigation.Directions
import uk.fernando.math.viewmodel.solo.CreateGameViewModel

@Composable
fun CreateGamePage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: CreateGameViewModel = getViewModel()
) {
    val coroutine = rememberCoroutineScope()
    val prefs: PrefsStore by inject()
    val isPremiumUser = prefs.isPremium().collectAsState(initial = false)

    val gamePrefs: GamePrefsStore by inject()
    val quantity = gamePrefs.quantity().collectAsState(initial = 10)
    val isMultipleChoice = gamePrefs.isMultipleChoice().collectAsState(initial = true)
    val difficulty = gamePrefs.difficulty().collectAsState(initial = 1)
    val operators = gamePrefs.getOperators().collectAsState(initial = listOf(1, 2, 3, 4))


    MyBackground {

        Column(Modifier.fillMaxSize()) {

            TopNavigationBar(
                title = R.string.question_creation_title,
                leftIcon = R.drawable.ic_arrow_back,
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

                        MyMathOperatorOptions(operators.value, isPremiumUser.value) {
                            viewModel.setMathOptions(it)
                        }

                        Divider(Modifier.padding(vertical = 16.dp))

                        MyQuestionQuantity(quantity.value) { quantity ->
                            viewModel.setQuantity(quantity)
                        }

                        Divider(Modifier.padding(vertical = 16.dp))

                        AnswerType(isMultipleChoice.value) { multipleChoice ->
                            viewModel.setTypeAnswer(multipleChoice)
                        }

                        Divider(Modifier.padding(vertical = 16.dp))

                        MyDifficulty(difficulty.value) { difficult ->
                            viewModel.setDifficulty(difficult)
                        }

                        Spacer(Modifier.height(60.dp))
                    }

                    CreateGameButton(viewModel) {
                        coroutine.launch {
                            viewModel.generateQuestion().collect {
                                if (it) navController.safeNav(Directions.game.name)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnswerType(isMultipleChoice: Boolean, onChecked: (Boolean) -> Unit) {
    var checked by mutableStateOf(isMultipleChoice)

    Column {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(
                checked = checked,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    uncheckedBorderColor = Color.Transparent,
                    uncheckedThumbColor = Color.White,
                ),
                onCheckedChange = {
                    onChecked(it)
                    checked = it
                })

            Text(
                modifier = Modifier.padding(start = 10.dp),
                style = MaterialTheme.typography.bodyLarge,
                text = stringResource(id = R.string.multiple_choice)
            )
        }
    }
}

@Composable
private fun BoxScope.CreateGameButton(viewModel: CreateGameViewModel, onClick: () -> Unit) {
    MyButton(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .defaultMinSize(minHeight = 50.dp),
        text = stringResource(id = R.string.start_action),
        isLoading = viewModel.loading.value,
        enabled = viewModel.isGameValid.value,
        onClick = onClick
    )
}