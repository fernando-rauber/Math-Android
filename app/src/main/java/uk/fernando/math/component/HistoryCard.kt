package uk.fernando.math.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uk.fernando.math.R
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.database.entity.PlayerEntity
import uk.fernando.math.ext.difficultyColor
import uk.fernando.math.ext.mathOperatorIcon
import uk.fernando.math.ext.timerFormat
import uk.fernando.math.ui.theme.star_green
import uk.fernando.math.ui.theme.star_red

@Composable
fun HistoryCard(modifier: Modifier = Modifier, history: HistoryEntity, player: PlayerEntity) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.height(IntrinsicSize.Min)
    ) {

        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(R.string.result),
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                Modifier.padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .border(2.dp, star_green, MaterialTheme.shapes.small)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "${player.correct}",
                        color = star_green,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .border(2.dp, star_red, MaterialTheme.shapes.small)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "${player.incorrect}",
                        color = star_red,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

            }

        }

        Divider(
            Modifier
                .fillMaxHeight()
                .width(1.dp)
        )

        Column(
            Modifier
                .weight(1f)
                .padding(start = 5.dp)
        ) {

            Row(
                Modifier
                    .padding(5.dp)
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_timer),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .weight(1f),
                    text = history.timer.timerFormat(),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .weight(1f),
                    horizontalArrangement = Arrangement.End
                ) {

                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        tint = history.difficulty.difficultyColor()
                    )

                    Icon(
                        painter = painterResource(if (history.difficulty == 1) R.drawable.ic_star_outline else R.drawable.ic_star),
                        contentDescription = null,
                        tint = history.difficulty.difficultyColor()
                    )

                    Icon(
                        painter = painterResource(if (history.difficulty <= 2) R.drawable.ic_star_outline else R.drawable.ic_star),
                        contentDescription = null,
                        tint = history.difficulty.difficultyColor()
                    )
                }
            }

            Divider(Modifier.padding(end = 5.dp))

            Row(
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {

                history.operatorList.forEach { operator ->
                    Icon(
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .size(18.dp),
                        painter = painterResource(operator.mathOperatorIcon()),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

            }
        }

    }
}