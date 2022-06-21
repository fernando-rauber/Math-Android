package uk.fernando.math.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import org.koin.androidx.compose.getViewModel
import uk.fernando.math.R
import uk.fernando.math.navigation.Directions
import uk.fernando.math.viewmodel.SplashViewModel

@Composable
fun SplashPage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: SplashViewModel = getViewModel()
) {
    Box(Modifier.fillMaxSize()) {

        Column(
            Modifier
                .offset(y = (-50).dp)
                .fillMaxWidth(0.8f)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .aspectRatio(1f)
            )

            Spacer( Modifier.height(35.dp))

            Text(
                text = stringResource(R.string.splash_title),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                lineHeight = 40.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }

    val isDarkMode = isSystemInDarkTheme()
    val currentOnTimeout by rememberUpdatedState {
        navController.navigate(Directions.history.name) {
            popUpTo(Directions.splash.name) { inclusive = true }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.firstSetUp(isDarkMode = isDarkMode)
        delay(1500L)
        currentOnTimeout()
    }
}
