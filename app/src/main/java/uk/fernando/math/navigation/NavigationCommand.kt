package uk.fernando.math.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface NavigationCommand {
    val name: String
    val arguments: List<NamedNavArgument>
}

object Directions {

    val splash = object : NavigationCommand {
        override val name: String
            get() = "splash"
        override val arguments: List<NamedNavArgument>
            get() = emptyList()
    }

    val createGame = object : NavigationCommand {
        override val name: String
            get() = "create_game"
        override val arguments: List<NamedNavArgument>
            get() = emptyList()
    }

    val game = object : NavigationCommand {
        override val name: String
            get() = "game"
        override val arguments: List<NamedNavArgument>
            get() = emptyList()
    }

    val history = object : NavigationCommand {
        override val name: String
            get() = "history"
        override val arguments: List<NamedNavArgument>
            get() = emptyList()
    }

    val summary = object : NavigationCommand {
        override val name: String
            get() = "summary"
        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(HISTORY_ID) { type = NavType.IntType }
            )
    }

    val settings = object : NavigationCommand {
        override val name: String
            get() = "settings"
        override val arguments: List<NamedNavArgument>
            get() = emptyList()
    }

    const val HISTORY_ID = "history_id"
}


