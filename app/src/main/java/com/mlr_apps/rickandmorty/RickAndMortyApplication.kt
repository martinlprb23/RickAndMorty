package com.mlr_apps.rickandmorty

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp//indica que creamos una funcion con hilt
class RickAndMortyApplication :Application(){
    override fun onCreate() {
        super.onCreate()
    }
}
//permite que genere nuestras clases para la inyeccion de dependencia