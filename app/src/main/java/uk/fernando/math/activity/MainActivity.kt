package uk.fernando.math.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.inject
import uk.fernando.math.component.BottomNavigationBar
import uk.fernando.math.component.UpdateStatusBar
import uk.fernando.math.datastore.PrefsStore
import uk.fernando.math.navigation.Directions
import uk.fernando.math.navigation.buildGraph
import uk.fernando.math.ui.theme.MyMathTheme
import uk.fernando.math.ui.theme.green_pastel

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val dataStore: PrefsStore by inject()
            val controller = rememberNavController()
            val navBackStackEntry by controller.currentBackStackEntryAsState()
            val isDarkMode = dataStore.isDarkMode().collectAsState(true)

            when (navBackStackEntry?.destination?.route) {
                Directions.splash.name, Directions.game.name, Directions.multiplayerGame.name -> UpdateStatusBar()
                Directions.history.name, Directions.multiplayerHistory.name -> UpdateStatusBar(green_pastel)
            }

            MyMathTheme(darkTheme = isDarkMode.value) {

                Scaffold(
                    bottomBar = {
                        when (navBackStackEntry?.destination?.route) {
                            Directions.history.name, Directions.multiplayerHistory.name, Directions.settings.name ->
                                BottomNavigationBar(controller)
                        }
                    },
                    backgroundColor = MaterialTheme.colorScheme.background
                ) { padding ->

                    Box(modifier = Modifier.padding(padding)) {
                        NavHost(
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