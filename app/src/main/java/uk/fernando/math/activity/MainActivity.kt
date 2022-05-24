package uk.fernando.math.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import uk.fernando.math.component.BottomNavigationBar
import uk.fernando.math.navigation.Directions
import uk.fernando.math.navigation.buildGraph
import uk.fernando.math.ui.theme.MyMathTheme
import uk.fernando.math.ui.theme.pastel_red

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val controller = rememberNavController()
            val navBackStackEntry by controller.currentBackStackEntryAsState()
            val decayAnimationSpec = rememberSplineBasedDecay<Float>()
            val scrollBehavior = remember(decayAnimationSpec) {
                TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
            }
            MyMathTheme {

                Scaffold(
//                    modifier = Modifier.background(
//                        Brush.verticalGradient(
//                        colors = listOf(
//                            MaterialTheme.colors.background.copy(0.9f),
//                            MaterialTheme.colors.background.copy(0.4f)
//                        )
//                    )),
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(pastel_red),
                            title = { Text("Large TopAppBar") },
                            navigationIcon = {
                                IconButton(onClick = { /* doSomething() */ }) {
                                    Icon(
                                        imageVector = Icons.Filled.Menu,
                                        contentDescription = "Localized description"
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = { /* doSomething() */ }) {
                                    Icon(
                                        imageVector = Icons.Filled.Favorite,
                                        contentDescription = "Localized description"
                                    )
                                }
                            },
                            scrollBehavior = scrollBehavior
                        )
                    },
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
                            startDestination = Directions.history.name
                        ) {
                            buildGraph(controller)
                        }

                    }
                }

            }
        }
    }
}