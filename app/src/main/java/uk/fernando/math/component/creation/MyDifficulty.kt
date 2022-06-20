package uk.fernando.math.component.creation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uk.fernando.math.R

@Composable
fun MyDifficulty(onSelected: (Int) -> Unit) {
    var difficulty by remember { mutableStateOf(1f) }

    Column {
        Text(
            text = stringResource(R.string.select_difficulty),
            style = MaterialTheme.typography.bodyLarge
        )

        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
        ) {
            Row(Modifier.weight(1f)) {
                Icon(Icons.Filled.Star, contentDescription = null)
            }
            Row(Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
                Icon(Icons.Filled.Star, contentDescription = null)
                Icon(Icons.Filled.Star, contentDescription = null)
            }
            Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                Icon(Icons.Filled.Star, contentDescription = null)
                Icon(Icons.Filled.Star, contentDescription = null)
                Icon(Icons.Filled.Star, contentDescription = null)
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