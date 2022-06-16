package uk.fernando.math.page.multiplayer

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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.getViewModel
import uk.fernando.math.R
import uk.fernando.math.component.HistoryCard
import uk.fernando.math.component.MultiplayerHistoryCard
import uk.fernando.math.component.MyBackground
import uk.fernando.math.component.TopNavigationBar
import uk.fernando.math.ui.theme.red
import uk.fernando.math.viewmodel.MultiplayerSummaryViewModel
import uk.fernando.math.viewmodel.SummaryViewModel

@ExperimentalMaterialApi
@Composable
fun MultiplayerSummaryPage(
    navController: NavController = NavController(LocalContext.current),
    historyID: Int,
    viewModel: MultiplayerSummaryViewModel = getViewModel()
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

                        MultiplayerHistoryCard(history = history)

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
//                            items(history.questionList) { question ->
//                                //MathCard(question.question, question.answer, question.correctAnswer)
//                            }
                        }
                    }
                }
            }
        }
    }
}