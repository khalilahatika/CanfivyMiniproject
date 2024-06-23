package org.d3if0047.canvufyminiproject.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object About : Screen("about")


}
