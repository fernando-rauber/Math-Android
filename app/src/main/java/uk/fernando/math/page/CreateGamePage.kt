package uk.fernando.math.page

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import uk.fernando.math.R
import uk.fernando.math.component.*
import uk.fernando.math.ext.TAG
import uk.fernando.math.ext.safeNav
import uk.fernando.math.navigation.Directions
import uk.fernando.math.ui.theme.green_pastel
import uk.fernando.math.ui.theme.red
import uk.fernando.math.viewmodel.CreateGameViewModel
import kotlin.math.roundToInt

@ExperimentalMaterialApi
@Composable
fun CreateGamePage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: CreateGameViewModel = getViewModel()
) {

    val coroutine = rememberCoroutineScope()

    Column(Modifier.fillMaxSize()) {

        TopNavigationBar(
            title = "Create Question",
            leftIcon = R.drawable.ic_arrow_back,
            onLeftIconClick = { navController.popBackStack() }
        )

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
                text = "Start",
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

@Composable
private fun BasicMathOptions(viewModel: CreateGameViewModel) {
    Column {
        //Text(text = "Math Operator")

        Row(Modifier.padding(vertical = 10.dp)) {
            MathOperatorIcon(R.drawable.ic_math_addition) { viewModel.setMathOptions(1) }
            MathOperatorIcon(R.drawable.ic_math_substraction) { viewModel.setMathOptions(2) }
            MathOperatorIcon(R.drawable.ic_math_division) { viewModel.setMathOptions(3) }
            MathOperatorIcon(R.drawable.ic_math_multiplication) { viewModel.setMathOptions(4) }
        }

        Row {
            MathOperatorIcon(R.drawable.ic_math_percentage) { viewModel.setMathOptions(5) }
            MathOperatorIcon(R.drawable.ic_math_square_root) { viewModel.setMathOptions(6) }

            Spacer(modifier = Modifier.weight(2f))
        }

//            MyCheckBox(title = "Fraction", isChecked = fraction, onCheckedChange = {
//                fraction = it
//                viewModel.setMathOptions(7)
//            })

    }
}

@Composable
private fun QuestionQuantity(onSelected: (Int) -> Unit) {
    var quantity by remember { mutableStateOf(10f) }

    Column {
        Text(text = "Quantity: ${quantity.toInt()}")

        Slider(
            modifier = Modifier.padding(vertical = 10.dp),
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
                text = stringResource(id = R.string.multiple_choices)
            )
        }
    }
}


@Composable
private fun Difficulty(onSelected: (Int) -> Unit) {
    var difficulty by remember { mutableStateOf(2f) }

    Column {
//        Text(text = "Choose your difficulty $difficulty")

        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Text(text = "Easy")
            Spacer(Modifier.weight(1f))
            Text(text = "Medium")
            Spacer(Modifier.weight(1f))
            Text(text = "Difficult")
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
private fun RowScope.MathOperatorIcon(@DrawableRes icon: Int, onChecked: () -> Unit) {
    var checked by remember { mutableStateOf(true) }
    IconToggleButton(
        modifier = Modifier.weight(1f),
        checked = checked,
        onCheckedChange = {
            onChecked()
            checked = it
        }) {
        if (checked)
            Icon(painterResource(id = icon), tint = green_pastel, contentDescription = "Localized description")
        else
            Icon(painterResource(id = icon), contentDescription = "Localized description")

    }
}