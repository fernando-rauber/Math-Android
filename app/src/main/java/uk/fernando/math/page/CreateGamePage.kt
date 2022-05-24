package uk.fernando.math.page

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import uk.fernando.math.R
import uk.fernando.math.component.*
import uk.fernando.math.ext.safeNav
import uk.fernando.math.navigation.Directions
import uk.fernando.math.ui.theme.green_pastel
import uk.fernando.math.ui.theme.red
import uk.fernando.math.viewmodel.CreateGameViewModel

@ExperimentalMaterialApi
@Composable
fun CreateGamePage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: CreateGameViewModel = getViewModel()
) {

    val coroutine = rememberCoroutineScope()

    Column(Modifier.fillMaxSize()) {

        TopNavigationBar(
            title = "Result",
            leftIcon = R.drawable.ic_arrow_back,
            onLeftIconClick = { navController.popBackStack() }
        )

        Column(Modifier.padding(16.dp)) {

            BasicMathOptions(viewModel)

            Divider()

            QuestionQuantity { quantity ->
                viewModel.setQuantity(quantity)
            }

            Divider()

            AnswerType { type ->
                viewModel.setTypeAnswer(type)
            }

            Divider()

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
    var addition by remember { mutableStateOf(true) }
    var subtraction by remember { mutableStateOf(true) }
    var multiplication by remember { mutableStateOf(true) }
    var division by remember { mutableStateOf(true) }

    // Premium Users
    var percentage by remember { mutableStateOf(true) }
    var square by remember { mutableStateOf(true) }
    var fraction by remember { mutableStateOf(true) }

    Column {
        Text(text = "Math Options")

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

    Column {
        Text(text = "How many question would you like")

        val quantity = listOf(
            RadioButtonData(R.string.quantity_1, 5),
            RadioButtonData(R.string.quantity_2, 10),
            RadioButtonData(R.string.quantity_3, 20)
        )

        MyRadioButton(items = quantity,
            itemSelected = quantity[1],
            onClick = { onSelected(it.value as Int) }
        )

    }
}

@Composable
private fun AnswerType(onSelected: (Int) -> Unit) {
    Column {
        Text(text = "Type of answer")

        val quantity = listOf(
            RadioButtonData(R.string.open_answer, 1),
            RadioButtonData(R.string.multiple_choices, 2),
        )

        MyRadioButton(items = quantity,
            itemSelected = quantity[1],
            onClick = { onSelected(it.value as Int) }
        )
    }
}


@Composable
private fun Difficulty(onSelected: (Int) -> Unit) {
    Column {
        Text(text = "Choose your difficulty")

        val difficulty = listOf(
            RadioButtonData(R.string.easy, 1),
            RadioButtonData(R.string.medium, 2),
            RadioButtonData(R.string.hard, 3)
        )

        MyRadioButton(items = difficulty,
            itemSelected = difficulty[1],
            onClick = { onSelected(it.value as Int) }
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