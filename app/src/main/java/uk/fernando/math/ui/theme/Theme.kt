package uk.fernando.math.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
//    primary = pastel_red,
//    primaryVariant = blue,
    background = dark,
    surface = lightDark,
    onBackground = Color.White,
)

private val LightColorPalette = lightColorScheme(

    surface = Color.White,
    background = whiteBackGround,
    onBackground = Color.Black,

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