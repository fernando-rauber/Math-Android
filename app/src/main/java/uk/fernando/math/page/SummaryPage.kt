package uk.fernando.math.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.koin.androidx.compose.getViewModel
import uk.fernando.math.R
import uk.fernando.math.component.HistoryCard
import uk.fernando.math.component.TopNavigationBar
import uk.fernando.math.ui.theme.green_pastel
import uk.fernando.math.viewmodel.SummaryViewModel

@ExperimentalMaterialApi
@Composable
fun SummaryPage(
    navController: NavController = NavController(LocalContext.current),
    historyID: Int,
    viewModel: SummaryViewModel = getViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.getHistory(historyID)
    }

    Column(Modifier.fillMaxSize()) {

        TopNavigationBar(title = "Result",
            rightIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = null
                    )
                }
            })

        viewModel.history.value?.let { history ->

            HistoryCard(Modifier.background(green_pastel), history = history.history)

            //User grid

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    start = 12.dp,
                    top = 16.dp,
                    end = 12.dp,
                    bottom = 16.dp
                ),
            ) {
                items(history.questionList) { question ->
                    MathCard(question.question, question.answer, question.correctAnswer)
                }
            }

//            LazyColumn(
//                modifier = Modifier.padding(10.dp),
//                contentPadding = PaddingValues(16.dp)
//            ) {
//
//                items(history.questionList) { question ->
//                    MathCard(question.question, question.answer, question.correctAnswer)
//                }
//            }
        }
    }
}

@Preview
@Composable
private fun MathCard(math: String = "10 + 10", answer: Int = 15, correctAnswer: Int = 20) {
    Row(Modifier.padding(top = 7.dp)) {
        Text(
            text = "$math = ",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "$answer",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (answer != correctAnswer) Color.Red else Color.Black
        )
    }
}