package uk.fernando.math.component.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import uk.fernando.math.ext.noRippleClickable
import kotlin.time.Duration.Companion.seconds

@Composable
fun MyCountDown(onStart: () -> Unit) {
    var countDown by remember { mutableStateOf(3) }

    LaunchedEffect(Unit) {
        while (countDown >= 0) {
            delay(1.seconds)
            countDown--
            if (countDown == -1)
                onStart()
        }
    }

    AnimatedVisibility(
        visible = countDown > 0,
        exit = fadeOut()
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .noRippleClickable {  }
                .background(Color.Black.copy(0.6f))
        ) {

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "$countDown",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 200.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}