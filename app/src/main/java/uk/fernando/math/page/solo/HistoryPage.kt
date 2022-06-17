package uk.fernando.math.page.solo

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import uk.fernando.math.R
import uk.fernando.math.component.*
import uk.fernando.math.database.entity.HistoryWithPLayers
import uk.fernando.math.database.entity.firstPlayer
import uk.fernando.math.ext.safeNav
import uk.fernando.math.navigation.Directions
import uk.fernando.math.viewmodel.solo.HistoryViewModel

@ExperimentalMaterialApi
@Composable
fun HistoryPage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: HistoryViewModel = getViewModel()
) {
    val coroutine = rememberCoroutineScope()

    MyBackground {

        Column(Modifier.fillMaxSize()) {

            TopNavigationBar(
                title = R.string.solo_title,
                rightIcon = {
                    TextButton(onClick = { navController.safeNav(Directions.createGame.name) }) {
                        Text(
                            text = stringResource(R.string.new_game_action),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground,
                            letterSpacing = (-0.80).sp
                        )
                    }
                })

            val historyList = viewModel.history.collectAsLazyPagingItems()

            Box(Modifier.weight(1f)) {

                if (historyList.loadState.refresh == LoadState.Loading)
                    LoadingHistory()
                else {
                    if (historyList.itemCount == 0)
                        EmptyHistory(
                            modifier = Modifier.fillMaxSize(),
                            message = R.string.empty_history_text,
                            onClick = { navController.safeNav(Directions.createGame.name) }
                        )
                    else
                        HistoryList(
                            modifier = Modifier
                                .padding(top = 30.dp)
                                .fillMaxSize(),
                            historyList = historyList,
                            onItemClick = { historyID ->
                                coroutine.launch {
                                    navController.safeNav(Directions.summary.name.plus("/$historyID"))
                                }
                            }
                        )
                }

                MyAdBanner(Modifier.align(Alignment.BottomCenter))
            }
        }
    }
}

@Composable
private fun HistoryList(modifier: Modifier, historyList: LazyPagingItems<HistoryWithPLayers>, onItemClick: (String) -> Unit) {
//    var date: Date = Date()

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 32.dp),
        modifier = modifier
    ) {

        items(historyList) { history ->
            history?.let {
                HistoryCardCustom(history) {
                    onItemClick("${history.history.id}")
                }
            }
        }
    }
}

//@Composable
//private fun DisplayDate(previousDate: Date, newDate: Date, onDateChange: (Date) -> Unit) {
//    if (!newDate.isSameDay(previousDate)) {
//        onDateChange(newDate)
//
//        if (!newDate.isSameDay(Date()))
//            Text(
//                modifier = Modifier.padding(top = 5.dp),
//                text = newDate.formatToDate(),
//                style = MaterialTheme.typography.bodyMedium,
//                fontWeight = FontWeight.Medium,
//                color = MaterialTheme.colorScheme.onBackground
//            )
//    }
//}

@Composable
fun EmptyHistory(modifier: Modifier, @StringRes message: Int, onClick: () -> Unit) {
    Box(modifier, contentAlignment = Alignment.Center) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(bottom = 15.dp),
                text = stringResource(message),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            MyButton(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 50.dp),
                onClick = onClick,
                text = stringResource(R.string.start_new_game_action)
            )
        }
    }
}

@Composable
fun BoxScope.LoadingHistory() {
    CircularProgressIndicator(
        strokeWidth = 5.dp,
        modifier = Modifier
            .align(Alignment.Center)
            .offset(y = (-70).dp)
            .fillMaxWidth(0.2f)
    )
}

@Composable
private fun HistoryCardCustom(history: HistoryWithPLayers, onClick: () -> Unit) {

    Surface(
        modifier = Modifier.padding(top = 10.dp),
        shadowElevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        HistoryCard(modifier = Modifier.clickable { onClick() }, history = history.history, history.firstPlayer())
    }
}