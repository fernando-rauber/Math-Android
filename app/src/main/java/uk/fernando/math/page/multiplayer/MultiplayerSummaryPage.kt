package uk.fernando.math.page.multiplayer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.getViewModel
import uk.fernando.math.R
import uk.fernando.math.component.MultiplayerHistoryCard
import uk.fernando.math.component.MyBackground
import uk.fernando.math.component.TopNavigationBar
import uk.fernando.math.database.entity.PlayerQuestionEntity
import uk.fernando.math.database.entity.firstPlayer
import uk.fernando.math.page.solo.MathCard
import uk.fernando.math.viewmodel.multiplayer.MultiplayerSummaryViewModel

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


                        val player1 = history.playerList[0]
                        val player2 = history.playerList[1]

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                        ) {
                            item {
                                Row {
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = player1.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center
                                    )

                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = player2.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            items(history.firstPlayer().questionList.size) { index ->
                                ResultMathCard(player1.questionList[index], player2.questionList[index])
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultMathCard(player1Question: PlayerQuestionEntity, player2Question: PlayerQuestionEntity) {
    Row {
        MathCard(Modifier.weight(1f), player1Question.question, player1Question.answer ?: 0, player1Question.correctAnswer)
        Spacer(Modifier.width(16.dp))
        MathCard(Modifier.weight(1f), player2Question.question, player2Question.answer ?: 0, player2Question.correctAnswer)
    }
}