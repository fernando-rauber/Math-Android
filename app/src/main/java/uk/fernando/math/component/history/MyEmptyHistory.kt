package uk.fernando.math.component.history

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uk.fernando.math.R
import uk.fernando.util.component.MyButton

@Composable
fun MyEmptyHistory(modifier: Modifier, @StringRes message: Int, onClick: () -> Unit) {
    Box(modifier, contentAlignment = Alignment.Center) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(bottom = 15.dp),
                text = stringResource(message),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            MyButton(
                modifier = Modifier
                    .padding(16.dp)
                    .defaultMinSize(minHeight = 50.dp),
                onClick = onClick,
                text = stringResource(R.string.start_new_game_action)
            )
        }
    }
}