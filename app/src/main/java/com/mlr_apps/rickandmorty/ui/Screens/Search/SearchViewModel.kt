package com.mlr_apps.rickandmorty.ui.Screens.Search

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
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
class SearchViewModel @Inject constructor(
    private val repository: RickAndMortyRepository
) :ViewModel(){


    private var page = 1

    var characterList = mutableStateOf<List<RickAndMortyListEntry>>(listOf())
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    var querySave = mutableStateOf("")
    var statusSave = mutableStateOf("")
    var genderSave = mutableStateOf("")


    fun searchCharacter(query: String, status:String, gender:String){

        viewModelScope.launch {

            if (querySave.value != query){
                page = 1
                characterList.value = emptyList()
            }

            querySave.value = query
            statusSave.value = status
            genderSave.value = gender


            if (query.isEmpty()){
                isSearching.value = false
                isSearchStarting = true
            }else{
                isSearching.value = true
            }

            val results =  repository.getCharacterByStatusAndGender(
                page = page,
                name = query,
                status = status,
                gender = gender
            )

            when(results){

                is Resource.Success -> {
                    endReached.value = page == results.data!!.info.pages
                    val rickAndMortyEntries = results.data.results.mapIndexed{ index, entry ->

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
                    //page ++
                    loadError.value = ""
                    isLoading.value = false
                    characterList.value += rickAndMortyEntries
                    Log.d("Size", page.toString())
                }

                is Resource.Error ->{
                    loadError.value = results.message!!
                    isLoading.value = false
                }
            }

            if (isSearchStarting){
                isSearchStarting = false
            }

            isSearching.value = false

        }

    }

    fun addPage(){
        page++
        searchCharacter(
            query = querySave.value,
            status = statusSave.value,
            gender = genderSave.value
        )
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