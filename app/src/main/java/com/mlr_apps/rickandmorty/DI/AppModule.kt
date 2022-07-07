package com.mlr_apps.rickandmorty.DI

import com.mlr_apps.rickandmorty.Data.API.RickAndMortyAPI
import com.mlr_apps.rickandmorty.Repository.RickAndMortyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object AppModule {

    @Singleton
    @Provides
    fun providePokemonRepository(
        api: RickAndMortyAPI
    )= RickAndMortyRepository(api)

    @Singleton
    @Provides
    fun provideRickAndMortyAPI():RickAndMortyAPI{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://rickandmortyapi.com/api/")
            .build()
            .create(RickAndMortyAPI::class.java)
    }
}