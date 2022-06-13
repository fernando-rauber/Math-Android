package uk.fernando.math.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun TopNavigationBar(
    @StringRes title: Int,
    @DrawableRes leftIcon: Int? = null,
    onLeftIconClick: () -> Unit = {},
    rightIcon: (@Composable () -> Unit)? = null
) {

    Box(
        Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(10.dp)
    ) {

        IconButton(onClick = onLeftIconClick, modifier = Modifier.align(Alignment.CenterStart)) {
            if (leftIcon != null)
                Icon(
                    painter = painterResource(leftIcon),
                    contentDescription = null
                )
        }

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(title),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge
        )

        if (rightIcon != null) {
            Box(Modifier.align(Alignment.CenterEnd)) {
                rightIcon()
            }
        }
    }
}