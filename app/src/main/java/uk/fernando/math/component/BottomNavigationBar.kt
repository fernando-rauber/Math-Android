package uk.fernando.math.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import uk.fernando.math.R
import uk.fernando.math.ext.safeNav
import uk.fernando.math.navigation.Directions
import uk.fernando.math.theme.game_green

@Composable
fun BottomNavigationBar(navController: NavController) {

    NavigationBar(
        modifier = Modifier
            .shadow(
                elevation = 14.dp,
                shape = MaterialTheme.shapes.large.copy(
                    bottomEnd = CornerSize(0.dp),
                    bottomStart = CornerSize(0.dp)
                ),
                ambientColor = Color.Black,
                spotColor = Color.Black
            ),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        NavigationItemCustom(currentRoute == Directions.history.name, R.drawable.ic_one_controller, R.string.solo_action) {
            if (currentRoute != Directions.history.name)
                navController.safeNav(Directions.history.name)
        }

        NavigationItemCustom(currentRoute == Directions.multiplayerHistory.name, R.drawable.ic_two_controller, R.string.multiplayer_action) {
            if (currentRoute != Directions.multiplayerHistory.name)
                navController.safeNav(Directions.multiplayerHistory.name)
        }

        NavigationItemCustom(currentRoute == Directions.settings.name, R.drawable.ic_settings, R.string.settings_action) {
            if (currentRoute != Directions.settings.name)
                navController.safeNav(Directions.settings.name)
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
    NavigationBarItem(
        icon = {
            Icon(
                painter = painterResource(id = iconID),
                contentDescription = null,
                tint = if (isSelected) game_green else MaterialTheme.colorScheme.onBackground
            )
        },
        colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.surface),
        selected = isSelected,
        label = {
            Text(
                text = stringResource(id = stringID),
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) game_green else MaterialTheme.colorScheme.onBackground
            )
        },
        onClick = onClick
    )
}