package uk.fernando.math.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColorScheme(
//    primary = pastel_red,
//    primaryVariant = blue,
)

private val LightColorPalette = lightColorScheme(


    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
//    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun MyMathTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    val colors = if (darkTheme)
        DarkColorPalette
    else
        LightColorPalette

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}