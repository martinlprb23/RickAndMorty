package com.mlr_apps.rickandmorty.Data.API.Response.Locations

data class LocationList(
    val info: Info,
    val results: List<Result>
)