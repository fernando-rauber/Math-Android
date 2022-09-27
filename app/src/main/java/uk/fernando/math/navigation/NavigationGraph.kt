package uk.fernando.math.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import uk.fernando.math.navigation.Directions.HISTORY_ID
import uk.fernando.math.page.SettingsPage
import uk.fernando.math.page.SplashPage
import uk.fernando.math.page.solo.*
import uk.fernando.math.page.multiplayer.MultiplayerCreateGamePage
import uk.fernando.math.page.multiplayer.MultiplayerGamePage
import uk.fernando.math.page.multiplayer.MultiplayerHistoryPage
import uk.fernando.math.page.multiplayer.MultiplayerSummaryPage


@ExperimentalAnimationApi
fun NavGraphBuilder.buildGraph(navController: NavController) {
    composable(Directions.splash.name) {
        SplashPage(navController)
    }
    composableSlideAnim(
        leftDirection = null,
        direction = Directions.history.name,
        rightDirection = Directions.multiplayerHistory.name,
        content = { HistoryPage(navController) }
    )

    composableSlideAnim(
        leftDirection = Directions.history.name,
        direction = Directions.multiplayerHistory.name,
        rightDirection = Directions.settings.name,
        content = {  MultiplayerHistoryPage(navController) }
    )

    composableSlideAnim(
        leftDirection = Directions.multiplayerHistory.name,
        direction = Directions.settings.name,
        rightDirection = null,
        content = {  SettingsPage() }
    )

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

}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.composableSlideAnim(leftDirection: String?, direction: String, rightDirection: String?, content: @Composable () -> Unit) {
    composable(direction,
        enterTransition = {
            when (initialState.destination.route) {
                rightDirection -> slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                leftDirection -> slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                else -> null
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                rightDirection -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                leftDirection -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                else -> null
            }
        }) {
        content()
    }
}


