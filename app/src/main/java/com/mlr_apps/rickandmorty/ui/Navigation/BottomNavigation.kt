package com.mlr_apps.rickandmorty.ui.Navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mlr_apps.rickandmorty.ui.theme.RickAndMortyTheme


@Composable
fun BottomNavigation(navController: NavHostController, currentRoute: String?) {
    val items = listOf(
        BottomNavItem.Characters,
        BottomNavItem.Locations,
        BottomNavItem.Episodes)


   RickAndMortyTheme {


       BottomNavigation(
           backgroundColor = MaterialTheme.colors.background,
           contentColor = MaterialTheme.colors.primary,
           modifier = Modifier
               .padding(start = 24.dp, bottom = 16.dp, end = 24.dp)
               .shadow(8.dp, RoundedCornerShape(50.dp))
               .clip(RoundedCornerShape(50.dp))
       ) {

           items.forEach { item ->
               BottomNavigationItem(
                   //icon = { Icon(painterResource(id = item.Icon), contentDescription = item.title) },
                   icon = {Icon(
                       imageVector = item.Icon,
                       contentDescription = null,
                       modifier = Modifier.size(24.dp)
                   )},
                   label = { Text(text = item.title, fontSize = 10.sp) },
                   selectedContentColor = MaterialTheme.colors.onBackground,
                   unselectedContentColor = MaterialTheme.colors.onBackground.copy(0.4f),
                   alwaysShowLabel = true,
                   onClick = {
                       if (currentRoute!=item.screenRoute){
                           navController.navigate(item.screenRoute) {

                               navController.graph.startDestinationRoute?.let { screen_route ->
                                   popUpTo(screen_route) {
                                       saveState = true
                                   }
                               }
                               launchSingleTop = true
                               restoreState = true
                           }
                       }
                   },
                   selected = currentRoute == item.screenRoute
               )
           }
       }
   }
}