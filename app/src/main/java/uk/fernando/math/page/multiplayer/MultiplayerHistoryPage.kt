package uk.fernando.math.page.multiplayer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import uk.fernando.math.R
import uk.fernando.math.component.MyAdBanner
import uk.fernando.math.component.MyBackground
import uk.fernando.math.component.TopNavigationBar
import uk.fernando.math.component.history.MultiplayerHistoryCard
import uk.fernando.math.component.history.MyEmptyHistory
import uk.fernando.math.component.history.MyLoadingHistory
import uk.fernando.math.component.history.NewGameButton
import uk.fernando.math.database.entity.HistoryWithPLayers
import uk.fernando.math.ext.safeNav
import uk.fernando.math.navigation.Directions
import uk.fernando.math.viewmodel.multiplayer.MultiplayerHistoryViewModel

@Composable
fun MultiplayerHistoryPage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: MultiplayerHistoryViewModel = getViewModel()
) {
    val coroutine = rememberCoroutineScope()

    MyBackground {

        Column {

            TopNavigationBar(
                title = R.string.score_title,
                rightIcon = { NewGameButton { navController.safeNav(Directions.multiplayerCreateGame.name) } })

            val historyList = viewModel.history.collectAsLazyPagingItems()

            Box(Modifier.fillMaxSize()) {

                if (historyList.loadState.refresh == LoadState.Loading) {
                    MyLoadingHistory()
                } else {
                    if (historyList.itemCount == 0)
                        MyEmptyHistory(
                            modifier = Modifier.fillMaxSize(),
                            message = R.string.empty_multiplayer_history_text,
                            onClick = { navController.safeNav(Directions.multiplayerCreateGame.name) }
                        )
                    else
                        HistoryList(
                            modifier = Modifier
                                .padding(top = 30.dp)
                                .fillMaxSize(),
                            historyList = historyList,
                            onItemClick = { historyID ->
                                coroutine.launch {
                                    navController.safeNav(Directions.multiplayerSummary.name.plus("/$historyID"))
                                }
                            }
                        )
                }

                MyAdBanner(Modifier.align(Alignment.BottomCenter), unitId = R.string.ad_banner_multiplayer)
            }
        }
    }
}

@Composable
private fun HistoryList(modifier: Modifier, historyList: LazyPagingItems<HistoryWithPLayers>, onItemClick: (String) -> Unit) {
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

@Composable
private fun HistoryCardCustom(history: HistoryWithPLayers, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.padding(top = 10.dp),
        shadowElevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        MultiplayerHistoryCard(modifier = Modifier.clickable { onClick() }, history = history)
    }
}