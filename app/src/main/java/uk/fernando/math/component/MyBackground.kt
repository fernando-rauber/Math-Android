package uk.fernando.math.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.fernando.math.theme.game_green

@Composable
fun MyBackground(content: @Composable () -> Unit) {
    Box {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f),
            shadowElevation = 4.dp,
            color = game_green,
            content = {}
        )

        content()
    }
}