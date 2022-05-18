package uk.fernando.math.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import uk.fernando.math.page.CreateGamePage
import uk.fernando.math.page.SettingsPage


@ExperimentalAnimationApi
@ExperimentalMaterialApi
fun NavGraphBuilder.buildGraph(navController: NavController) {
    composable(Directions.splash.name) {

    }
    composable(Directions.createGame.name) {
        CreateGamePage(navController)
    }
    composable(Directions.history.name) {
    }
    composable(Directions.settings.name) {
        SettingsPage()
    }
}


