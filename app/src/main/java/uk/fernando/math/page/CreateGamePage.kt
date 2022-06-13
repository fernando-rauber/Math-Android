package uk.fernando.math.page

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import uk.fernando.math.R
import uk.fernando.math.component.MyBackground
import uk.fernando.math.component.MyButton
import uk.fernando.math.component.TopNavigationBar
import uk.fernando.math.ext.safeNav
import uk.fernando.math.navigation.Directions
import uk.fernando.math.ui.theme.green_pastel
import uk.fernando.math.viewmodel.CreateGameViewModel

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

                Column(Modifier.padding(16.dp)) {

                    BasicMathOptions(viewModel)

                    Divider(Modifier.padding(vertical = 16.dp))

                    QuestionQuantity { quantity ->
                        viewModel.setQuantity(quantity)
                    }

                    Divider(Modifier.padding(vertical = 16.dp))

                    AnswerType { multipleChoice ->
                        viewModel.setTypeAnswer(multipleChoice)
                    }

                    Divider(Modifier.padding(vertical = 16.dp))

                    Difficulty { difficult ->
                        viewModel.setDifficulty(difficult)
                    }

                    Spacer(Modifier.weight(1f))

                    MyButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 50.dp),
                        text = stringResource(id = R.string.start_action),
                        isLoading = viewModel.loading.value,
                        onClick = {
                            coroutine.launch {
                                viewModel.generateQuestion().collect {
                                    if (it) navController.safeNav(Directions.game.name)
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
private fun BasicMathOptions(viewModel: CreateGameViewModel) {
    Column {
        Text(
            text = stringResource(id = R.string.select_operator),
            style = MaterialTheme.typography.bodyLarge
        )

        Row(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MathOperatorIcon(R.drawable.ic_math_addition) { viewModel.setMathOptions(1) }
            MathOperatorIcon(R.drawable.ic_math_substraction) { viewModel.setMathOptions(2) }
            MathOperatorIcon(R.drawable.ic_math_division) { viewModel.setMathOptions(3) }
            MathOperatorIcon(R.drawable.ic_math_multiplication) { viewModel.setMathOptions(4) }
        }

        Row(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MathOperatorIcon(R.drawable.ic_math_percentage, false) { viewModel.setMathOptions(5) }
            MathOperatorIcon(R.drawable.ic_math_square_root, false) { viewModel.setMathOptions(6) }

            Icon(painterResource(R.drawable.ic_math_percentage), contentDescription = null, tint = Color.Transparent)
            Icon(painterResource(R.drawable.ic_math_percentage), contentDescription = null, tint = Color.Transparent)
        }
    }
}

@Composable
private fun QuestionQuantity(onSelected: (Int) -> Unit) {
    var quantity by remember { mutableStateOf(10f) }

    Column {
        Text(
            text = stringResource(R.string.quantity, "${quantity.toInt()}"),
            style = MaterialTheme.typography.bodyLarge
        )

        Slider(
            modifier = Modifier.padding(vertical = 5.dp),
            value = quantity,
            onValueChange = {
                quantity = it
                onSelected(it.toInt())
            },
            steps = 4,
            valueRange = 5f..30f,
        )
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
private fun Difficulty(onSelected: (Int) -> Unit) {
    var difficulty by remember { mutableStateOf(1f) }

    Column {
        Text(
            text = stringResource(R.string.select_difficulty),
            style = MaterialTheme.typography.bodyLarge
        )

        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
        ) {
            Row(Modifier.weight(1f)) {
                Icon(Icons.Filled.Star, contentDescription = null)
            }
            Row(Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
                Icon(Icons.Filled.Star, contentDescription = null)
                Icon(Icons.Filled.Star, contentDescription = null)
            }
            Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                Icon(Icons.Filled.Star, contentDescription = null)
                Icon(Icons.Filled.Star, contentDescription = null)
                Icon(Icons.Filled.Star, contentDescription = null)
            }
        }

        Slider(
            value = difficulty,
            onValueChange = {
                difficulty = it
                onSelected(it.toInt())
            },
            steps = 1,
            valueRange = 1f..3f,
        )

    }
}

@Composable
private fun MathOperatorIcon(@DrawableRes icon: Int, isChecked: Boolean = true, onChecked: () -> Unit) {
    var checked by remember { mutableStateOf(isChecked) }
    IconToggleButton(
        checked = checked,
        onCheckedChange = {
            onChecked()
            checked = it
        }) {
        if (checked)
            Icon(painterResource(id = icon), tint = green_pastel, contentDescription = null)
        else
            Icon(painterResource(id = icon), contentDescription = null)

    }
}