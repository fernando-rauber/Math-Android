package uk.fernando.math.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun MyCheckBox(title: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )

        Text(text = title)

    }
}