package uk.fernando.math.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import org.koin.androidx.compose.inject
import uk.fernando.math.component.BottomNavigationBar
import uk.fernando.math.datastore.PrefsStore
import uk.fernando.math.navigation.Directions
import uk.fernando.math.navigation.buildGraph
import uk.fernando.math.theme.MyMathTheme
import uk.fernando.math.theme.dark
import uk.fernando.math.theme.game_green
import uk.fernando.math.theme.whiteBackGround
import uk.fernando.util.component.UpdateStatusBar

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        MediationTestSuite.launch(this)

        setContent {
            val controller = rememberAnimatedNavController()
            val navBackStackEntry by controller.currentBackStackEntryAsState()

            val dataStore: PrefsStore by inject()
            val isDarkMode = dataStore.isDarkMode().collectAsState(true)
            val backgroundStatusBar = if (isDarkMode.value) dark else whiteBackGround

            // Status Bar color
            when (navBackStackEntry?.destination?.route) {
                Directions.splash.name, Directions.game.name, Directions.multiplayerGame.name -> UpdateStatusBar(backgroundStatusBar)
                Directions.history.name, Directions.multiplayerHistory.name,
                Directions.createGame.name, Directions.multiplayerCreateGame.name -> UpdateStatusBar(game_green)
            }

            MyMathTheme(darkTheme = isDarkMode.value) {

                Scaffold(
                    bottomBar = {
                        when (navBackStackEntry?.destination?.route) {
                            Directions.history.name, Directions.multiplayerHistory.name, Directions.settings.name ->
                                BottomNavigationBar(controller)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.background
                ) { padding ->

                    Box(modifier = Modifier.padding(padding)) {
                        AnimatedNavHost(
                            navController = controller,
                            startDestination = Directions.splash.name
                        ) {
                            buildGraph(controller)
                        }
                    }
                }
            }
        }
    }
}