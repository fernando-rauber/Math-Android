package uk.fernando.math.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.fernando.math.ui.theme.grey
import uk.fernando.math.ui.theme.orange

@Composable
fun MyButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true,
    color: Color = orange,
    fontSize: TextUnit = 17.sp,
    isLoading: Boolean = false,
    textModifier: Modifier = Modifier,
    borderStroke: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    elevation: ButtonElevation = ButtonDefaults.buttonElevation(4.dp, 0.dp)
) {
    Button(
        border = borderStroke,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = color, disabledContentColor = grey),
        elevation = elevation,
        contentPadding = contentPadding,
        onClick = { if (!isLoading) onClick() }
    ) {
        if (isLoading)
            CircularProgressIndicator(
                strokeWidth = 3.dp,
                color = Color.White,
                modifier = Modifier.size(30.dp)
            )
        else
            Text(
                modifier = textModifier,
                text = text,
                textAlign = TextAlign.Center,
                color = if (enabled) Color.White else Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = fontSize
            )
    }
}
