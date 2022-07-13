package uk.fernando.math.component.creation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uk.fernando.math.R
import uk.fernando.math.theme.game_green

@Composable
fun MyMathOperatorOptions(operator: List<Int>, isPremium: Boolean, onItemSelected: (Int) -> Unit) {
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
            MathOperatorIcon(R.drawable.ic_math_addition, isChecked = operator.contains(1)) { onItemSelected(1) }
            MathOperatorIcon(R.drawable.ic_math_substraction, isChecked = operator.contains(2)) { onItemSelected(2) }
            MathOperatorIcon(R.drawable.ic_math_division, isChecked = operator.contains(3)) { onItemSelected(3) }
            MathOperatorIcon(R.drawable.ic_math_multiplication, isChecked = operator.contains(4)) { onItemSelected(4) }
        }

        Row(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MathOperatorIcon(R.drawable.ic_math_percentage, isPremium, isChecked = operator.contains(5)) { onItemSelected(5) }
            MathOperatorIcon(R.drawable.ic_math_square_root, isPremium, isChecked = operator.contains(6)) { onItemSelected(6) }
            MathOperatorIcon(R.drawable.ic_math_more_than, isPremium, isChecked = operator.contains(7)) { onItemSelected(7) }
            MathOperatorIcon(R.drawable.ic_math_less_than, isPremium, isChecked = operator.contains(8)) { onItemSelected(8) }
        }
    }
}

@Composable
private fun MathOperatorIcon(@DrawableRes icon: Int, hasPremium: Boolean = true, isChecked: Boolean = true, onChecked: () -> Unit) {
    var checked by mutableStateOf(isChecked)

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
                tint = if (checked) game_green else LocalContentColor.current,
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