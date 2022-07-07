package com.mlr_apps.rickandmorty.ui.Screens.Episodes

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mlr_apps.rickandmorty.Data.API.Response.Episode.EpisodeList
import com.mlr_apps.rickandmorty.Data.Model.EpisodesListEntry
import com.mlr_apps.rickandmorty.Repository.RickAndMortyRepository
import com.mlr_apps.rickandmorty.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EpisodesViewModel
@Inject
constructor(private val  repository: RickAndMortyRepository):ViewModel() {

    private var pageEpisode = 1

    var episodeList = mutableStateOf<List<EpisodesListEntry>>(listOf())
    var loadError = mutableStateOf(value = "")
    var isLoading = mutableStateOf(value = false)
    var endReached = mutableStateOf(value = false)

    init {
        episodesPaginated()
    }

     fun episodesPaginated() {
        viewModelScope.launch {
            isLoading.value = true

            when(val result = repository.getEpisodes(pageId = pageEpisode)){
                is Resource.Success ->{

                    endReached.value = pageEpisode ==result.data!!.info.pages

                    val episodesEntries = result.data.results.mapIndexed{ _, entry ->
                        EpisodesListEntry(
                            episodeName = entry.name.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(Locale.ROOT)
                                else it.toString()
                            },
                            airDate = entry.air_date,
                            episode = entry.episode,
                            characters = entry.characters.size
                        )
                    }

                    pageEpisode++
                    loadError.value = ""
                    isLoading.value = false
                    episodeList.value +=episodesEntries

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