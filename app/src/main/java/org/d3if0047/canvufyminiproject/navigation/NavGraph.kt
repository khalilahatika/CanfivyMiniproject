package org.d3if0047.canvufyminiproject.navigation

import Mainscreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.d3if0047.canvufyminiproject.ui.screen.CmToPixel
import org.d3if0047.canvufyminiproject.ui.screen.CmToPixelScreen
import org.d3if0047.canvufyminiproject.ui.screen.MmToPixel
import org.d3if0047.canvufyminiproject.ui.screen.MmToPixelScreen

import org.d3if0047.canvufyminiproject.ui.screen.PalleteScreen


@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()){
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ){
        composable(route = Screen.Home.route){
            Mainscreen(navController)
        }

        composable(route = Screen.Pallete.route){
            PalleteScreen(navController)
        }
        composable(route = Screen.CmToPixel.route) {
            CmToPixel(navController)
        }
        composable(route = Screen.MmToPixel.route) {
            MmToPixel(navController)
        }

    }
}