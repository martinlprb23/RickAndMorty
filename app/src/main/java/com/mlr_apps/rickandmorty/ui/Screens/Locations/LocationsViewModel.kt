package com.mlr_apps.rickandmorty.ui.Screens.Locations

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mlr_apps.rickandmorty.Data.Model.LocationsListEntry
import com.mlr_apps.rickandmorty.Repository.RickAndMortyRepository
import com.mlr_apps.rickandmorty.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel
@Inject
constructor(
    private val repository: RickAndMortyRepository
):ViewModel(){
    private var pageLocation = 1

    var locationsList = mutableStateOf<List<LocationsListEntry>>(listOf())
    var loadError = mutableStateOf(value = "")
    var isLoading = mutableStateOf(value = false)
    var endReached = mutableStateOf(value = false)

    init {
        loadLocationsPaginated()
    }

    fun loadLocationsPaginated() {
        viewModelScope.launch {
            isLoading.value = true
            when(val result = repository.getLocations(pageId = pageLocation)){
                is Resource.Success ->{
                    endReached.value = pageLocation  == result.data!!.info.pages

                    val locationsEntries = result.data.results.mapIndexed{ _, entry ->

                        LocationsListEntry(
                            locationName = entry.name.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(Locale.ROOT)
                                else it.toString()
                        },
                            type = entry.type,
                            dimension = entry.dimension,
                            id = entry.id)
                    }
                    pageLocation++
                    loadError.value = ""
                    isLoading.value = false
                    locationsList.value += locationsEntries
                }

                is Resource.Error ->{
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                else -> {}
            }
        }
    }


}



































