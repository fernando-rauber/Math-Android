package uk.fernando.math.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import uk.fernando.math.ui.theme.greyDark

@Preview(showBackground = true)
@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    headColor: Color = greyDark,
    onValueChange: (String) -> Unit = {},
    readOnly: Boolean = false,
    enable: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions : KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier) {

        OutlinedTextField(
            value = value,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onValueChange,
            label = label,
            visualTransformation = visualTransformation,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.outlinedTextFieldColors(
//                containerColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                disabledTextColor = greyDark,
                unfocusedBorderColor = greyDark.copy(ContentAlpha.disabled),
                focusedBorderColor = MaterialTheme.colors.primary,
                cursorColor = MaterialTheme.colors.primary,
            ),
            readOnly = readOnly,
            enabled = enable
        )

    }
}