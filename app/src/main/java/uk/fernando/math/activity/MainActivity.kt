package uk.fernando.math.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.inject
import uk.fernando.math.component.BottomNavigationBar
import uk.fernando.math.datastore.PrefsStore
import uk.fernando.math.navigation.Directions
import uk.fernando.math.navigation.buildGraph
import uk.fernando.math.ui.theme.MyMathTheme

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val dataStore: PrefsStore by inject()
            val controller = rememberNavController()
            val navBackStackEntry by controller.currentBackStackEntryAsState()
            val isDarkMode = dataStore.isDarkMode().collectAsState(true)

            MyMathTheme(darkTheme = isDarkMode.value) {

                Scaffold(
//                    modifier = Modifier.background(
//                        Brush.verticalGradient(
//                        colors = listOf(
//                            MaterialTheme.colors.background.copy(0.9f),
//                            MaterialTheme.colors.background.copy(0.4f)
//                        )
//                    )),
                    bottomBar = {
                        when (navBackStackEntry?.destination?.route) {
                            Directions.history.name, Directions.settings.name ->
                                BottomNavigationBar(controller)
                        }
                    },
                    backgroundColor = Color.Transparent
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