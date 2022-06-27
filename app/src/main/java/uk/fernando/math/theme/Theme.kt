package uk.fernando.math.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = green_pastel,
    background = dark,
    surface = lightDark,
//    onSurface = red,
    onBackground = Color.White,
//    onPrimary = Color.Red,
    onSecondary = Color.Yellow,
    onPrimaryContainer = Color.Blue,
    secondary = Color.Green,
    primaryContainer = Color.White
)

private val LightColorPalette = lightColorScheme(

    primary = green_pastel,
    surface = Color.White,
    background = whiteBackGround,
    onBackground = Color.Black,
    onSurfaceVariant = grey

    /* Other default colors to override
    background = Color.White,
//    surface = Color.White,
//    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
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