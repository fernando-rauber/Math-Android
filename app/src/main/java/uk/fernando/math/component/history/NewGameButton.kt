package uk.fernando.math.component.history

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.fernando.math.R

@Composable
fun NewGameButton(onClick: () -> Unit) {
    Text(
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .border(
                1.dp,
                MaterialTheme.colorScheme.onBackground,
                MaterialTheme.shapes.small
            )
            .clickable { onClick() }
            .padding(horizontal = 7.dp, vertical = 3.dp),
        text = stringResource(R.string.new_game_action),
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onBackground,
        letterSpacing = (-0.80).sp
    )
}