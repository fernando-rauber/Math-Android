package uk.fernando.math.component.history

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.MyLoadingHistory() {
    CircularProgressIndicator(
        strokeWidth = 5.dp,
        modifier = Modifier
            .align(Alignment.Center)
            .offset(y = (-70).dp)
            .fillMaxWidth(0.2f)
    )
}