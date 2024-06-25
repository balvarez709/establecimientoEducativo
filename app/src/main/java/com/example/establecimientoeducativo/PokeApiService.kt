package com.example.establecimientoeducativo

import androidx.compose.ui.input.pointer.PointerEvent
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response

interface PokeApiService {
    @GET("pokemon/{name}")
    suspend fun getPokemon(
        @Path("name") name: String
    ): Response<Pokemon>

    @GET("pokemon-species/{name}")
    suspend fun getPokemonSpecies(
        @Path("name") name: String
    ): Response<Pokemon>

    @GET("ability/{name}")
    suspend fun getAbility(
        @Path("name") name: String
    ): Response<Pokemon>
}