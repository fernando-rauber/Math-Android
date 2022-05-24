package uk.fernando.math.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRadioButton(
    items: List<RadioButtonData>,
    itemSelected: RadioButtonData,
    onClick: (RadioButtonData) -> Unit
) {
    var selected by remember { mutableStateOf(itemSelected) }

    val onSelectedChange = { item: RadioButtonData ->
        selected = item
        onClick(item)
    }

    Row {
        items.forEach { mItem ->
            val isSelected = selected == mItem

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.selectable(
                    selected = isSelected,
                    onClick = { onSelectedChange(mItem) },
                ),
            ) {

                RadioButton(
                    selected = isSelected,
                    onClick = { onSelectedChange(mItem) }
                )

                Text(
                    text = stringResource(id = mItem.title),
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(start = 8.dp, end = 30.dp),
                )
            }
        }
    }
}

data class RadioButtonData(@StringRes val title: Int, val value: Any)