package uk.fernando.math.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import uk.fernando.math.R
import uk.fernando.math.component.MyButton
import uk.fernando.math.component.TopNavigationBar
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.ext.difficultyColor
import uk.fernando.math.ext.difficultyName
import uk.fernando.math.ext.safeNav
import uk.fernando.math.ext.timerFormat
import uk.fernando.math.navigation.Directions
import uk.fernando.math.ui.theme.green_pastel
import uk.fernando.math.ui.theme.orange
import uk.fernando.math.ui.theme.pastel_red
import uk.fernando.math.viewmodel.HistoryViewModel
import java.util.*

@ExperimentalMaterialApi
@Composable
fun HistoryPage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: HistoryViewModel = getViewModel()
) {
    val coroutine = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {

        TopNavigationBar(title = "History")

        MyButton(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 50.dp),
            color = green_pastel,
            onClick = { navController.safeNav(Directions.createGame.name) },
            text = "New Game"
        )

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.weight(0.9f)
        ) {

            items(viewModel.history.value) { history ->

                HistoryCard(history) {
                    coroutine.launch {
                        navController.safeNav(Directions.summary.name.plus("/${history.id}"))
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun preview() {
    HistoryCard(history = HistoryEntity(1, Date(), 135, 2, 6, 3)) {

    }
}

@Composable
private fun HistoryCard(history: HistoryEntity, onClick: () -> Unit) {

    Surface(
        modifier = Modifier
            .padding(top = 10.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick() },
        color = orange,
        shadowElevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(IntrinsicSize.Min)
        ) {

            Column(
                Modifier
                    .padding(10.dp)
                    .padding(start = 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_timer),
                    contentDescription = null,
                    tint = Color.White
                )
                Text(
                    text = history.timer.timerFormat(),
                    color = Color.White
                )
            }

            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )

            Row(
                Modifier
                    .padding(10.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .background(green_pastel, MaterialTheme.shapes.small)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "${history.correct}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .background(pastel_red, MaterialTheme.shapes.small)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "${history.incorrect}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.7f)
                    .background(history.difficulty.difficultyColor())
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    color = Color.White,
                    fontSize = 16.sp,
                    text = stringResource(id = history.difficulty.difficultyName())
                )
            }

        }
    }
}