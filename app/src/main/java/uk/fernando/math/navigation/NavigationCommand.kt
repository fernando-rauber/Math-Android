package uk.fernando.math.navigation

import androidx.navigation.NamedNavArgument

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

    val settings = object : NavigationCommand {
        override val name: String
            get() = "settings"
        override val arguments: List<NamedNavArgument>
            get() = emptyList()
    }
}


