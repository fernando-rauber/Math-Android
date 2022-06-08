package uk.fernando.math.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.inject
import uk.fernando.math.R
import uk.fernando.math.datastore.PrefsStore
import uk.fernando.math.navigation.Directions
import uk.fernando.math.viewmodel.SettingsViewModel
import uk.fernando.math.viewmodel.SplashViewModel

@Composable
fun SplashPage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: SplashViewModel = getViewModel()
) {

    Box(Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.mipmap.ic_launcher),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(0.4f)
                .offset(y =(-50).dp)
                .align(Alignment.Center)
        )
    }

    val isDarkMode = isSystemInDarkTheme()
    val currentOnTimeout by rememberUpdatedState {
        navController.navigate(Directions.history.name) {
            popUpTo(Directions.splash.name) { inclusive = true }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.firstInstall(isDarkMode = isDarkMode)
        delay(1500L)
        currentOnTimeout()
    }
}
