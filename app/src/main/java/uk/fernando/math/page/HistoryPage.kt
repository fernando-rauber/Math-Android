package uk.fernando.math.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.inject
import uk.fernando.advertising.component.AdBanner
import uk.fernando.math.R
import uk.fernando.math.component.HistoryCard
import uk.fernando.math.component.MyBackground
import uk.fernando.math.component.MyButton
import uk.fernando.math.component.TopNavigationBar
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.datastore.PrefsStore
import uk.fernando.math.ext.formatToDate
import uk.fernando.math.ext.isSameDay
import uk.fernando.math.ext.safeNav
import uk.fernando.math.navigation.Directions
import uk.fernando.math.viewmodel.HistoryViewModel
import java.util.*

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
                title = R.string.history_title,
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

            Box(Modifier.weight(1f)) {

                if (!viewModel.isLoading.value && viewModel.history.value.isEmpty())
                    EmptyHistory(
                        modifier = Modifier.fillMaxSize(),
                        onClick = { navController.safeNav(Directions.createGame.name) }
                    )
                else
                    HistoryList(
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .fillMaxSize(),
                        viewModel = viewModel,
                        onItemClick = { historyID ->
                            coroutine.launch {
                                navController.safeNav(Directions.summary.name.plus("/$historyID"))
                            }
                        }
                    )

                AdBanner(Modifier.align(Alignment.BottomCenter))
            }
        }
    }

}

@Composable
private fun HistoryList(modifier: Modifier, viewModel: HistoryViewModel, onItemClick: (String) -> Unit) {
    var date: Date = Date()

    SwipeRefresh(
        state = rememberSwipeRefreshState(viewModel.isLoading.value),
        onRefresh = viewModel::getAllHistory
    ) {

        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 32.dp),
            modifier = modifier
        ) {

            items(viewModel.history.value) { history ->
                HistoryCardCustom(history) {
                    onItemClick("${history.id}")
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
private fun EmptyHistory(modifier: Modifier, onClick: () -> Unit) {
    Box(modifier, contentAlignment = Alignment.Center) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(bottom = 15.dp),
                text = stringResource(R.string.empty_history_text),
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
private fun AdBanner(modifier: Modifier) {
    val dataStore: PrefsStore by inject()

    val isPremium = dataStore.isPremium().collectAsState(true)

    if (!isPremium.value)
        AdBanner(
            unitId = stringResource(R.string.ad_banner),
            modifier = modifier.padding(bottom = 8.dp)
        )
}

@Composable
private fun HistoryCardCustom(history: HistoryEntity, onClick: () -> Unit) {

    Surface(
        modifier = Modifier.padding(top = 10.dp),
        shadowElevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        HistoryCard(modifier = Modifier.clickable { onClick() }, history = history)
    }
}