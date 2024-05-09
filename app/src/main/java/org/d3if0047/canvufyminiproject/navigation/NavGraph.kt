package org.d3if0047.canvufyminiproject.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.d3if0047.canvufyminiproject.ui.screen.DetailPallete
import org.d3if0047.canvufyminiproject.ui.screen.KEY_ID_PALET
import org.d3if0047.canvufyminiproject.ui.screen.Mainscreen
import org.d3if0047.canvufyminiproject.ui.screen.PalleteScreen


@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()){
    NavHost(
        navController = navController,
        startDestination = Screen.Pallete.route
    ){
        composable(route = Screen.Pallete.route){
            PalleteScreen(navController)
        }
        composable(route = Screen.Home.route){
            Mainscreen(navController)
        }

        composable(route = Screen.FormBaru.route) {
            DetailPallete(navController)
        }

        composable( route = Screen.FormUbah.route,
            arguments = listOf(
                navArgument(KEY_ID_PALET){ type = NavType.LongType }
            )

        ){navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getLong(KEY_ID_PALET)
            DetailPallete(navController,id)

        }

    }


}
