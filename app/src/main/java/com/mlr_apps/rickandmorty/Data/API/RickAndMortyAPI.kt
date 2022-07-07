package com.mlr_apps.rickandmorty.Data.API

import com.mlr_apps.rickandmorty.Data.API.Response.CharacterInfo.CharacterInfo
import com.mlr_apps.rickandmorty.Data.API.Response.Episode.EpisodeList
import com.mlr_apps.rickandmorty.Data.API.Response.Locations.LocationList
import com.mlr_apps.rickandmorty.Data.API.Response.RickAndMortyList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyAPI {

    @GET("character")
    suspend fun getRickAndMortyList(
        @Query("page") page : Int,
    ):RickAndMortyList

    @GET("character/{character_id}")
    suspend fun getCharacterInfo(
        @Path("character_id") characterId: Int
    ):CharacterInfo

    @GET("character")
    suspend fun getCharacterByStatusAndGender(
        @Query("page") page : Int,
        @Query("name") name:String,
        @Query("status") status:String,
        @Query("gender") gender:String,
    ):RickAndMortyList

    @GET("location")
    suspend fun getLocations(
        @Query("page") page: Int,
    ):LocationList

    @GET("episode")
    suspend fun getEpisodes(
        @Query("page") page: Int,
    ):EpisodeList


}