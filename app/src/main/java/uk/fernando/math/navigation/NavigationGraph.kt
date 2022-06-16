package uk.fernando.math.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import uk.fernando.math.navigation.Directions.HISTORY_ID
import uk.fernando.math.page.*
import uk.fernando.math.page.multiplayer.MultiplayerCreateGamePage
import uk.fernando.math.page.multiplayer.MultiplayerGamePage
import uk.fernando.math.page.multiplayer.MultiplayerHistoryPage
import uk.fernando.math.page.multiplayer.MultiplayerSummaryPage


@ExperimentalAnimationApi
@ExperimentalMaterialApi
fun NavGraphBuilder.buildGraph(navController: NavController) {
    composable(Directions.splash.name) {
        SplashPage(navController)
    }
    composable(Directions.createGame.name) {
        CreateGamePage(navController)
    }
    composable(Directions.multiplayerCreateGame.name) {
        MultiplayerCreateGamePage(navController)
    }
    composable(Directions.game.name) {
        GamePage(navController)
    }
    composable(Directions.multiplayerGame.name) {
        MultiplayerGamePage(navController)
    }
    composable(Directions.summary.name.plus("/{$HISTORY_ID}")) {
        val historyID = it.arguments?.getString(HISTORY_ID)
        if (historyID == null)
            navController.popBackStack()
        else
            SummaryPage(navController, historyID.toInt())
    }
    composable(Directions.multiplayerSummary.name.plus("/{$HISTORY_ID}")) {
        val historyID = it.arguments?.getString(HISTORY_ID)
        if (historyID == null)
            navController.popBackStack()
        else
            MultiplayerSummaryPage(navController, historyID.toInt())
    }
    composable(Directions.history.name) {
        HistoryPage(navController)
    }
    composable(Directions.multiplayerHistory.name) {
        MultiplayerHistoryPage(navController)
    }
    composable(Directions.settings.name) {
        SettingsPage()
    }
}


