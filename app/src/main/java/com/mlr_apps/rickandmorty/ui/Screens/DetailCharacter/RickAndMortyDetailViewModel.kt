package com.mlr_apps.rickandmorty.ui.Screens.DetailCharacter

import android.util.Log
import androidx.lifecycle.ViewModel
import com.mlr_apps.rickandmorty.Data.API.Response.CharacterInfo.CharacterInfo
import com.mlr_apps.rickandmorty.Data.API.Response.Episode.Result
import com.mlr_apps.rickandmorty.Repository.RickAndMortyRepository
import com.mlr_apps.rickandmorty.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class RickAndMortyDetailViewModel
@Inject
constructor(
    private val repository: RickAndMortyRepository
): ViewModel(){

    suspend fun getCharacterInfo(characterId: Int):Resource<CharacterInfo>{
        return repository.getCharacterInfo(characterId)
    }

}
