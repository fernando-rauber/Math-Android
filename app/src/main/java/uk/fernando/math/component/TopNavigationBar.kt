package uk.fernando.math.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopNavigationBar(
    title: String,
    @DrawableRes leftIcon: Int? = null,
    onLeftIconClick: () -> Unit = {},
    rightIcon: (@Composable () -> Unit)? = null
) {

    Surface(elevation = 4.dp) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp)
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
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            if (rightIcon != null) {
                Box(Modifier.align(Alignment.CenterEnd)) {
                    rightIcon()
                }
            }
        }
    }
}