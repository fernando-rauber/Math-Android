package uk.fernando.math.page

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import uk.fernando.math.R
import uk.fernando.math.component.MyButton
import uk.fernando.math.component.MyCheckBox
import uk.fernando.math.component.MyRadioButton
import uk.fernando.math.component.RadioButtonData
import uk.fernando.math.ext.safeNav
import uk.fernando.math.navigation.Directions
import uk.fernando.math.viewmodel.CreateGameViewModel

@ExperimentalMaterialApi
@Composable
fun CreateGamePage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: CreateGameViewModel = getViewModel()
) {

    val coroutine = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

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

        Row {
            MyCheckBox(title = "Addition", isChecked = addition, onCheckedChange = {
                addition = it
                viewModel.setMathOptions(1)
            })
            MyCheckBox(title = "Subtraction", isChecked = subtraction, onCheckedChange = {
                subtraction = it
                viewModel.setMathOptions(2)
            })
        }

        Row {
            MyCheckBox(title = "Division", isChecked = division, onCheckedChange = {
                division = it
                viewModel.setMathOptions(3)
            })
            MyCheckBox(title = "Multiplication", isChecked = multiplication, onCheckedChange = {
                multiplication = it
                viewModel.setMathOptions(4)
            })
        }

        Row {
            MyCheckBox(title = "Percentage", isChecked = percentage, onCheckedChange = {
                percentage = it
                viewModel.setMathOptions(5)
            })
            MyCheckBox(title = "Square", isChecked = square, onCheckedChange = {
                square = it
                viewModel.setMathOptions(6)
            })
            MyCheckBox(title = "Fraction", isChecked = fraction, onCheckedChange = {
                fraction = it
                viewModel.setMathOptions(7)
            })
        }

    }
}

@Composable
private fun QuestionQuantity(onSelected: (Int) -> Unit) {

    Column {
        Text(text = "How many question would you like")

        val quantity = listOf(
            RadioButtonData(R.string.quantity_1, 1),
            RadioButtonData(R.string.quantity_2, 2),
            RadioButtonData(R.string.quantity_3, 3)
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