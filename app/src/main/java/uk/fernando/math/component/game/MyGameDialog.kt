package uk.fernando.math.component.game

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.fernando.math.R
import uk.fernando.math.component.MyButton
import uk.fernando.math.component.MyDialog
import uk.fernando.math.theme.star_red

@Composable
fun MyGameDialog(
    @DrawableRes image: Int,
    @StringRes message: Int,
    @StringRes buttonText: Int,
    onExitGame: (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    MyDialog {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                modifier = Modifier.padding(vertical = 30.dp).fillMaxWidth(0.6f),
                painter = painterResource(id = image),
                contentDescription = null,
                tint = Color.Unspecified
            )

            Text(
                modifier = Modifier.padding(vertical = 15.dp),
                text = stringResource(message),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 40.sp,
                letterSpacing = 0.30.sp
            )

            MyButton(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 50.dp),
                onClick = onClick,
                text = stringResource(buttonText)
            )

            if (onExitGame != null)
                MyButton(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 50.dp),
                    onClick = onExitGame,
                    color = star_red,
                    text = stringResource(R.string.exit_game_action)
                )
        }
    }
}