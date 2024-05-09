package org.d3if0047.canvufyminiproject.navigation

import org.d3if0047.canvufyminiproject.ui.screen.KEY_ID_PALET

sealed class Screen (val route:String) {
    data object Home: Screen("mainScreen")
    data object Pallete : Screen("palleteScreen")

    data object FormBaru: Screen("detailScreen")

    data object FormUbah: Screen("detailPallete/{$KEY_ID_PALET}"){
        fun withID(id: Long) = "detailPallete/$id"
    }


}