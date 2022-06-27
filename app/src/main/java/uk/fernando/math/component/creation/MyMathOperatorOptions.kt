package uk.fernando.math.component.creation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.inject
import uk.fernando.math.R
import uk.fernando.math.datastore.PrefsStore
import uk.fernando.math.theme.green_pastel

@Composable
fun MyMathOperatorOptions(onItemSelected: (Int) -> Unit) {
    val dataStore: PrefsStore by inject()

    val isPremium = dataStore.isPremium().collectAsState(false)

    Column {
        Text(
            text = stringResource(id = R.string.select_operator),
            style = MaterialTheme.typography.bodyLarge
        )

        Row(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MathOperatorIcon(R.drawable.ic_math_addition) { onItemSelected(1) }
            MathOperatorIcon(R.drawable.ic_math_substraction) { onItemSelected(2) }
            MathOperatorIcon(R.drawable.ic_math_division) { onItemSelected(3) }
            MathOperatorIcon(R.drawable.ic_math_multiplication) { onItemSelected(4) }
        }

        Row(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MathOperatorIcon(R.drawable.ic_math_percentage, isPremium.value, isChecked = false) { onItemSelected(5) }
            MathOperatorIcon(R.drawable.ic_math_square_root, isPremium.value, isChecked = false) { onItemSelected(6) }
            MathOperatorIcon(R.drawable.ic_math_more_than, isPremium.value, isChecked = false) { onItemSelected(7) }
            MathOperatorIcon(R.drawable.ic_math_less_than, isPremium.value, isChecked = false) { onItemSelected(8) }
        }
    }
}

@Composable
private fun MathOperatorIcon(@DrawableRes icon: Int, hasPremium: Boolean = true, isChecked: Boolean = true, onChecked: () -> Unit) {
    var checked by remember { mutableStateOf(isChecked) }
    IconToggleButton(
        checked = checked,
        onCheckedChange = {
            if (hasPremium) {
                onChecked()
                checked = it
            }
        }) {
        Box {
            Icon(
                painterResource(id = icon),
                tint = if (checked) green_pastel else LocalContentColor.current,
                contentDescription = null
            )

            if (!hasPremium)
                Icon(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .rotate(45f)
                        .offset(y = (-20).dp),
                    painter = painterResource(id = R.drawable.ic_crown),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
        }
    }
}