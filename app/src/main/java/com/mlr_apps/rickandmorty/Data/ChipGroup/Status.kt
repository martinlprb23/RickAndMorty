package com.mlr_apps.rickandmorty.Data.ChipGroup

// Falta terminar mi Pana
enum  class Status(val value:String){
    alive("Alive"),
    dead("Dead"),
    unknown("Unknown"),
}

fun getAllStatus(): List<Status>{
    return listOf(Status.alive, Status.dead, Status.unknown)
}

fun getStatus(value: String): Status? {
    val map = Status.values().associateBy(Status::value)
    return map[value]
}
