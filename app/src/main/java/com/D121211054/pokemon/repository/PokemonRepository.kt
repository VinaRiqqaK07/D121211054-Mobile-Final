package com.D121211054.pokemon.repository

import com.D121211054.pokemon.network.ApiService
import model.PokemonDetails
import javax.inject.Inject


class PokemonRepository @Inject constructor(private val apiService: ApiService) {

    // Implement logic to fetch Pokemon data from ApiService
    suspend fun getPokemonDetails(id: Int): PokemonDetails {
        return apiService.getPokemonDetails(id)
    }

    // Add more methods as needed based on your requirements
}