package uk.fernando.math.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.compose.inject
import uk.fernando.math.datastore.PrefsStore
import uk.fernando.math.ui.theme.green_pastel

@Composable
fun UpdateStatusBar(color: Color? = null) {
    val systemUiController = rememberSystemUiController()
    val systemBarColor = MaterialTheme.colorScheme.background

    SideEffect {
        systemUiController.setStatusBarColor(
            color = color ?: systemBarColor,
        )
    }
}