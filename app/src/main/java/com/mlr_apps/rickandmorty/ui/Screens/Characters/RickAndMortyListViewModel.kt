package com.mlr_apps.rickandmorty.ui.Screens.Characters

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.mlr_apps.rickandmorty.Data.Model.RickAndMortyListEntry
import com.mlr_apps.rickandmorty.Repository.RickAndMortyRepository
import com.mlr_apps.rickandmorty.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RickAndMortyListViewModel
@Inject
constructor(
    private val repository: RickAndMortyRepository
):ViewModel(){
    private var page = 1

    var rickAndMortyList = mutableStateOf<List<RickAndMortyListEntry>>(listOf())
    var loadError = mutableStateOf(value = "")
    var isLoading = mutableStateOf(value = false)
    var endReached = mutableStateOf(false)

    init {
        loadRickAndMortyPaginated()
    }

    fun loadRickAndMortyPaginated(){
        viewModelScope.launch {
            isLoading.value = true
            when(val result = repository.getRickAndMortyList(page )){
                is Resource.Success -> {
                    endReached.value = page == result.data!!.info.pages

                    val rickAndMortyEntries = result.data.results.mapIndexed{index, entry ->

                        val number = if(entry.url.endsWith("/")){
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        }else{
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        val url = "https://rickandmortyapi.com/api/character/avatar/${number}.jpeg"

                        RickAndMortyListEntry( characterName = entry.name.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.ROOT)
                            else it.toString()
                        },
                            number = number.toInt(),
                            status = entry.status,
                            imageUrl = url,
                            origin = entry.origin.name,
                            gender = entry.gender)
                    }
                    page++
                    loadError.value = ""
                    isLoading.value = false
                    rickAndMortyList.value += rickAndMortyEntries
                }

                is Resource.Error ->{
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }




    fun calcDominantColor(drawable: Drawable, onFinish: (Color) ->Unit){
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate{ pallete ->
            pallete?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }

}

