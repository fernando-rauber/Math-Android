package uk.fernando.math.page.multiplayer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.getViewModel
import uk.fernando.math.R
import uk.fernando.math.component.MyBackground
import uk.fernando.math.component.TopNavigationBar
import uk.fernando.math.component.history.MultiplayerHistoryCard
import uk.fernando.math.database.entity.QuestionEntity
import uk.fernando.math.database.entity.firstPlayer
import uk.fernando.math.page.solo.MathCard
import uk.fernando.math.theme.game_green
import uk.fernando.math.viewmodel.multiplayer.MultiplayerSummaryViewModel
import uk.fernando.util.component.MyIconButton
import uk.fernando.util.component.UpdateStatusBar

@Composable
fun MultiplayerSummaryPage(
    navController: NavController = NavController(LocalContext.current),
    historyID: Int,
    viewModel: MultiplayerSummaryViewModel = getViewModel()
) {
    UpdateStatusBar(color = game_green)

    LaunchedEffect(Unit) {
        viewModel.getHistory(historyID)
    }

    MyBackground {

        Column(Modifier.fillMaxSize()) {

            TopNavigationBar(title = R.string.summary_title,
                rightIcon = {
                    MyIconButton(
                        icon = R.drawable.ic_close,
                        onClick = { navController.popBackStack() },
                        tint = Color.White
                    )
                })

            Surface(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                shadowElevation = 7.dp,
                shape = MaterialTheme.shapes.medium.copy(bottomEnd = CornerSize(0f), bottomStart = CornerSize(0f))
            ) {
                viewModel.history.value?.let { history ->

                    Column {

                        MultiplayerHistoryCard(history = history)

                        Divider(Modifier.padding(bottom = 5.dp))

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
                                Row(Modifier.padding(bottom = 10.dp)) {
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
private fun ResultMathCard(player1Question: QuestionEntity, player2Question: QuestionEntity) {
    Row {
        MathCard(Modifier.weight(1f), player1Question, size = 13)
        Spacer(Modifier.width(8.dp))
        MathCard(Modifier.weight(1f), player2Question, size = 13)
    }
}