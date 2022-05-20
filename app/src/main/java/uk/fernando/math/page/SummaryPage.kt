package uk.fernando.math.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import org.koin.androidx.compose.getViewModel
import uk.fernando.math.ui.theme.orange
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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {

        viewModel.history.value?.let { history ->

            Row(
                Modifier
                    .fillMaxWidth()
                    .background(orange)
                    .weight(0.1f)
            ) {

                Text(text = "Total:" + (history.history.correct + history.history.incorrect))
                Text(text = "Correct:" + history.history.correct)
                Text(text = "Incorrect:" + history.history.incorrect)

            }


            LazyColumn(
                state = rememberLazyListState(),
                modifier = Modifier.weight(0.9f)
            ) {

                items(history.questionList) { question ->
                    Text(text = question.question)
                }
            }
        }
    }
}