package uk.fernando.math.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import uk.fernando.math.component.HistoryCard
import uk.fernando.math.component.MyButton
import uk.fernando.math.component.TopNavigationBar
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.ext.safeNav
import uk.fernando.math.navigation.Directions
import uk.fernando.math.ui.theme.*
import uk.fernando.math.viewmodel.HistoryViewModel

@ExperimentalMaterialApi
@Composable
fun HistoryPage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: HistoryViewModel = getViewModel()
) {
    val coroutine = rememberCoroutineScope()

    Column(Modifier.fillMaxSize()) {

        TopNavigationBar(title = "History")

        MyButton(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 50.dp),
            color = green_pastel,
            onClick = { navController.safeNav(Directions.createGame.name) },
            text = "New Game"
        )

        Divider(Modifier.padding(16.dp))

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.weight(0.9f)
        ) {

            items(viewModel.history.value) { history ->

                HistoryCardCustom(history) {
                    coroutine.launch {
                        navController.safeNav(Directions.summary.name.plus("/${history.id}"))
                    }
                }

            }
        }
    }
}

@Composable
private fun HistoryCardCustom(history: HistoryEntity, onClick: () -> Unit) {

    Surface(
        modifier = Modifier
            .padding(top = 10.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick() },
        color = blueDark,
        shadowElevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        HistoryCard(history = history)
    }
}