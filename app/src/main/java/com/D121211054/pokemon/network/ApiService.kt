package com.D121211054.pokemon.network

import model.PokemonDetails
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("pokemon/{id}/")
    suspend fun getPokemonDetails(@Path("id") id: Int): PokemonDetails
    // ...
}
