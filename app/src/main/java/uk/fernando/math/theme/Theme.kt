package uk.fernando.math.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = game_green,
    background = dark,
    surface = lightDark,
    onBackground = Color.White,
    primaryContainer = Color.White
)

private val LightColorPalette = lightColorScheme(
    primary = game_green,
    surface = Color.White,
    background = whiteBackGround,
    onBackground = Color.Black,
    onSurfaceVariant = grey
)

@Composable
fun MyMathTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorPalette else LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}