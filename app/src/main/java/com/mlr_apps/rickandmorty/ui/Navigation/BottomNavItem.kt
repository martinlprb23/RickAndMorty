package com.mlr_apps.rickandmorty.ui.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationCity
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Tv
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomNavItem(
    var title: String,
    var Icon: ImageVector,
    var screenRoute: String
){

    object Characters : BottomNavItem("Characters", Icons.Rounded.Person,"characters")
    object Locations: BottomNavItem("Locations",Icons.Rounded.LocationCity,"locations")
    object Episodes: BottomNavItem("Episodes",Icons.Rounded.Tv,"episodes")

}