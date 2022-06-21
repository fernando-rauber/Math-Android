package uk.fernando.math.component.history

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uk.fernando.math.R
import uk.fernando.math.database.entity.HistoryWithPLayers
import uk.fernando.math.database.entity.PlayerEntity
import uk.fernando.math.ext.difficultyColor
import uk.fernando.math.ext.mathOperatorIcon
import uk.fernando.math.ui.theme.star_green
import uk.fernando.math.ui.theme.star_red

@Composable
fun MultiplayerHistoryCard(modifier: Modifier = Modifier, history: HistoryWithPLayers) {
    Column(modifier.height(IntrinsicSize.Min)) {

        history.playerList.forEach { player ->
            PlayerCard(player)
            Divider(Modifier.padding(3.dp))
        }
        Row(
            modifier = Modifier
                .padding(10.dp)
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {

            history.history.operatorList.forEach { operator ->
                Icon(
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .size(22.dp),
                    painter = painterResource(operator.mathOperatorIcon()),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(Modifier.weight(1f))

            Icon(
                Icons.Filled.Star,
                contentDescription = null,
                tint = history.history.difficulty.difficultyColor()
            )

            Icon(
                painter = painterResource(if (history.history.difficulty == 1) R.drawable.ic_star_outline else R.drawable.ic_star),
                contentDescription = null,
                tint = history.history.difficulty.difficultyColor()
            )

            Icon(
                painter = painterResource(if (history.history.difficulty <= 2) R.drawable.ic_star_outline else R.drawable.ic_star),
                contentDescription = null,
                tint = history.history.difficulty.difficultyColor()
            )
        }
    }
}

@Composable
private fun PlayerCard(player: PlayerEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .weight(0.3f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(Modifier.padding(vertical = 8.dp)) {

                Box(
                    modifier = Modifier
                        .weight(1f)
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

                Spacer(modifier = Modifier.width(5.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
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

        Text(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .weight(0.7f),
            text = player.name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}