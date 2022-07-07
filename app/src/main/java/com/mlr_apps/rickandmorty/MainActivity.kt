package com.mlr_apps.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mlr_apps.rickandmorty.ui.Navigation.BottomNavItem
import com.mlr_apps.rickandmorty.ui.Navigation.BottomNavigation
import com.mlr_apps.rickandmorty.ui.Screens.DetailCharacter.CharacterInfoScreen
import com.mlr_apps.rickandmorty.ui.Screens.Characters.MainCharacters
import com.mlr_apps.rickandmorty.ui.Screens.Episodes.EpisodesScreen
import com.mlr_apps.rickandmorty.ui.Screens.Locations.LocationsScreen
import com.mlr_apps.rickandmorty.ui.Screens.Search.SearchCharacter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            if(isSystemInDarkTheme()){
                systemUiController.setNavigationBarColor(Color.Black, darkIcons = false)
                systemUiController.setStatusBarColor(Color.Black, darkIcons = false)
            }
            MainScreenView()
        }
    }
}


@Composable
fun MainScreenView(){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        bottomBar = {
            if (currentRoute!= "search_character" &&
                currentRoute != "Rick_And_Morty_detail_screen/{dominantColor}/{characterId}")
                {
                BottomNavigation(navController, currentRoute)
            }
        }

    ) {
        Screens(navController = navController)
    }
}


@Composable
fun Screens(navController: NavHostController) {
    //val navControler = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Characters.screenRoute
    ) {


        composable(BottomNavItem.Characters.screenRoute) {
            MainCharacters(navController = navController)
        }

        composable(BottomNavItem.Locations.screenRoute) {
            LocationsScreen()
        }

        composable(BottomNavItem.Episodes.screenRoute) {
            EpisodesScreen()
        }


        //Screens separates

        composable(route = "search_character"){
            SearchCharacter(navController = navController)
        }

        composable(
            route = "Rick_And_Morty_detail_screen/{dominantColor}/{characterId}",
            arguments = listOf(
                navArgument("dominantColor"){type = NavType.IntType},
                navArgument("characterId"){type = NavType.IntType}
            )
        ){
            val dominantColor = remember{
                val color = it.arguments?.getInt("dominantColor")
                color?.let { Color(it) } ?: Color.White
            }

            val characterId = remember {
                it.arguments?.getInt("characterId")
            }

            CharacterInfoScreen(
                dominantColor = dominantColor,
                characterId = characterId!!,
                navController = navController)
                //characterName = characterName?.toLowerCase(Locale.current)?:"")

        }

    }
}



