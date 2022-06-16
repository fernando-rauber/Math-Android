package uk.fernando.math.page

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.getViewModel
import uk.fernando.math.R
import uk.fernando.math.component.HistoryCard
import uk.fernando.math.component.MyBackground
import uk.fernando.math.component.TopNavigationBar
import uk.fernando.math.ui.theme.red
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

    MyBackground {

        Column(Modifier.fillMaxSize()) {

            TopNavigationBar(title = R.string.summary_title,
                rightIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                })

            Surface(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .fillMaxSize(),
                shadowElevation = 7.dp,
                shape = MaterialTheme.shapes.medium.copy(bottomEnd = CornerSize(0f), bottomStart = CornerSize(0f))
            ) {
                viewModel.history.value?.let { history ->

                    Column {

                        HistoryCard(history = history.history)

                        Divider(Modifier.padding(vertical = 5.dp))

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            text = stringResource(R.string.question_answer),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                        ) {
                            items(history.questionList) { question ->
                                MathCard(Modifier, question.question, question.answer, question.correctAnswer)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MathCard(modifier: Modifier = Modifier, math: String, answer: Int?, correctAnswer: Int) {

    Surface(
        modifier = modifier
            .padding(top = 10.dp)
            .fillMaxWidth(),
        shadowElevation = 5.dp,
        tonalElevation = 5.dp,
        shape = MaterialTheme.shapes.small
    ) {

        Row(
            Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$math = ",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${answer ?: " "}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = if (answer != correctAnswer) red else Color.Unspecified
            )
            if (answer != correctAnswer)
                Text(
                    modifier = Modifier.align(Alignment.Top),
                    text = "($correctAnswer)",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                )
        }
    }
}