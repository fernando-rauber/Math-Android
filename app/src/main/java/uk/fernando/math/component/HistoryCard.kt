package uk.fernando.math.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.fernando.math.R
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.ext.difficultyColor
import uk.fernando.math.ext.difficultyName
import uk.fernando.math.ext.timerFormat
import uk.fernando.math.ui.theme.green_pastel
import uk.fernando.math.ui.theme.pastel_red

@Composable
fun HistoryCard(modifier: Modifier = Modifier,history: HistoryEntity) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.height(IntrinsicSize.Min)
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
