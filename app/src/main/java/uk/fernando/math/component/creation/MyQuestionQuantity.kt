package uk.fernando.math.component.creation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uk.fernando.math.R

@Composable
fun MyQuestionQuantity(oldQuantity: Int, onSelected: (Int) -> Unit) {
    var quantity by mutableStateOf(oldQuantity.toFloat())

    Column {
        Text(
            text = stringResource(R.string.quantity, "${quantity.toInt()}"),
            style = MaterialTheme.typography.bodyLarge
        )

        Slider(
            modifier = Modifier.padding(vertical = 5.dp),
            value = quantity,
            onValueChange = {
                quantity = it
                onSelected(it.toInt())
            },
            steps = 2,
            valueRange = 5f..20f,
        )
    }
}