package uk.fernando.math.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import uk.fernando.math.R

@Composable
fun TopNavigationBar(
    @StringRes title: Int,
    onLeftIconClick: (() -> Unit)? = null,
    rightIcon: (@Composable () -> Unit)? = null
) {

    Box(
        Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(10.dp)
    ) {

        if (onLeftIconClick != null)
            IconButton(onClick = onLeftIconClick, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = null,
                    tint = Color.White
                )
            }

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(title),
            color = Color.White,
            style = MaterialTheme.typography.titleLarge
        )

        if (rightIcon != null) {
            Box(Modifier.align(Alignment.CenterEnd)) {
                rightIcon()
            }
        }
    }

    Spacer(Modifier.height(20.dp))
}