package uk.fernando.math.page.solo

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.getViewModel
import uk.fernando.math.R
import uk.fernando.math.component.MyBackground
import uk.fernando.math.component.TopNavigationBar
import uk.fernando.math.component.game.MyQuestion
import uk.fernando.math.component.history.HistoryCard
import uk.fernando.math.database.entity.QuestionEntity
import uk.fernando.math.database.entity.firstPlayer
import uk.fernando.math.ext.isBooleanQuestion
import uk.fernando.math.ext.toFalseTrue
import uk.fernando.math.ui.theme.red
import uk.fernando.math.viewmodel.solo.SummaryViewModel

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

                        HistoryCard(history = history.history, player = history.firstPlayer())

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
                            items(history.firstPlayer().questionList) { question ->
                                MathCard(Modifier, question)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MathCard(modifier: Modifier = Modifier, question: QuestionEntity) {

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

            val isBooleanAnswer = question.operator.isBooleanQuestion()

            val answer = if (question.answer == null) " "
            else if (isBooleanAnswer) stringResource(question.answer!!.toFalseTrue())
            else question.answer.toString()

            MyQuestion(
                value1 = question.value1,
                value2 = question.value2,
                operator = question.operator,
                result = answer,
                resultColor = if (question.answer != question.correctAnswer) red else null,
                size = 16
            )

            if (question.answer != question.correctAnswer) {
                val correctAnswer = if (isBooleanAnswer) stringResource(question.correctAnswer.toFalseTrue()) else question.correctAnswer
                Text(
                    modifier = Modifier.align(Alignment.Top),
                    text = "($correctAnswer)",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}