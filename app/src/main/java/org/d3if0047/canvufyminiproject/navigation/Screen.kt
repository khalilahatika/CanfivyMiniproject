package org.d3if0047.canvufyminiproject.navigation

sealed class Screen (val route:String) {
    data object Home: Screen("mainScreen")
    data object Pallete : Screen("palleteScreen")
    object CmToPixel : Screen("pixel_to_cm")
    object MmToPixel : Screen("pixel_to_mm")
}