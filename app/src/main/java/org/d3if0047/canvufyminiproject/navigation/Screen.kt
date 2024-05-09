package org.d3if0047.canvufyminiproject.navigation

sealed class Screen (val route:String) {
    data object Home: Screen("mainScreen")
    data object Pallete : Screen("palleteScreen")
}