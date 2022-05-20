package uk.fernando.math.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import uk.fernando.math.R
import uk.fernando.math.ext.safeNav
import uk.fernando.math.navigation.Directions


@Composable
fun BottomNavigationBar(navController: NavController) {
    Box {

        Surface(
            modifier = Modifier
                .shadow(5.dp)
                .align(Alignment.BottomCenter),
            elevation = 16.dp,
            shape = MaterialTheme.shapes.medium.copy(
                bottomEnd = CornerSize(0.dp),
                bottomStart = CornerSize(0.dp)
            )
        ) {

            BottomNavigation(
                modifier = Modifier,
//                backgroundColor = blue
            ) {
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                NavigationItemCustom(currentRoute == Directions.history.name, R.drawable.ic_calculate, R.string.history_action) {
                    if (currentRoute != Directions.history.name)
                        navController.safeNav(Directions.history.name)
                }

//                NavigationItemCustom(currentRoute == Directions.createGame.name, R.drawable.ic_calculate, R.string.game_action) {
//                    if (currentRoute != Directions.createGame.name)
//                        navController.safeNav(Directions.createGame.name)
//                }

                NavigationItemCustom(currentRoute == Directions.settings.name, R.drawable.ic_calculate, R.string.settings_action) {
                    if (currentRoute != Directions.settings.name)
                        navController.safeNav(Directions.settings.name)
                }
            }
        }
    }
}


@Composable
fun RowScope.NavigationItemCustom(
    isSelected: Boolean,
    @DrawableRes iconID: Int,
    @StringRes stringID: Int,
    onClick: () -> Unit
) {
    BottomNavigationItem(
        icon = {
            Icon(
                painter = painterResource(id = iconID),
                contentDescription = null
            )
        },
        selected = isSelected,
        label = {
            Text(
                text = stringResource(id = stringID),
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
            )
        },
        onClick = onClick
    )
}