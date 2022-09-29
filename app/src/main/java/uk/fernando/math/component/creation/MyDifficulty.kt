package uk.fernando.math.component.creation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uk.fernando.math.R

@Composable
fun MyDifficulty(oldDifficulty: Int, onSelected: (Int) -> Unit) {
    var difficulty by mutableStateOf(oldDifficulty.toFloat())

    Column {
        Text(
            text = stringResource(R.string.select_difficulty),
            style = MaterialTheme.typography.bodyLarge
        )

        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
        ) {
            val star = @Composable { Icon(painterResource(R.drawable.ic_star_outline), contentDescription = null) }

            Row(Modifier.weight(1f)) {
                star()
            }
            Row(Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
                star()
                star()
            }
            Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                star()
                star()
                star()
            }
        }

        Slider(
            value = difficulty,
            onValueChange = {
                difficulty = it
                onSelected(it.toInt())
            },
            steps = 1,
            valueRange = 1f..3f,
        )
    }
}