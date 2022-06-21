package uk.fernando.math.page.solo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
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
import uk.fernando.math.R
import uk.fernando.math.component.MyBackground
import uk.fernando.math.component.MyButton
import uk.fernando.math.component.TopNavigationBar
import uk.fernando.math.component.creation.MyDifficulty
import uk.fernando.math.component.creation.MyMathOperatorOptions
import uk.fernando.math.component.creation.MyQuestionQuantity
import uk.fernando.math.ext.safeNav
import uk.fernando.math.navigation.Directions
import uk.fernando.math.viewmodel.solo.CreateGameViewModel

@ExperimentalMaterialApi
@Composable
fun CreateGamePage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: CreateGameViewModel = getViewModel()
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
                            .verticalScroll(rememberScrollState())
                    ) {

                        MyMathOperatorOptions {
                            viewModel.setMathOptions(it)
                        }

                        Divider(Modifier.padding(vertical = 16.dp))

                        MyQuestionQuantity { quantity ->
                            viewModel.setQuantity(quantity)
                        }

                        Divider(Modifier.padding(vertical = 16.dp))

                        AnswerType { multipleChoice ->
                            viewModel.setTypeAnswer(multipleChoice)
                        }

                        Divider(Modifier.padding(vertical = 16.dp))

                        MyDifficulty { difficult ->
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
private fun AnswerType(onChecked: (Boolean) -> Unit) {
    Column {
        var checked by remember { mutableStateOf(true) }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(checked = checked, onCheckedChange = {
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