package com.D121211054.pokemon.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.D121211054.pokemon.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import model.PokemonDetails
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(private val repository: PokemonRepository) : ViewModel() {

    // Define LiveData or other properties as needed
    // For example, you can have a LiveData to hold Pokemon details
    private val _pokemonList = mutableStateListOf<PokemonDetails>()
    val pokemonList: List<PokemonDetails> get() = _pokemonList

    init {
        viewModelScope.launch {
            repeat(3) {
                val pokemon = repository.getPokemonDetails(it+1)
                _pokemonList.add(pokemon)
            }
        }
    }

    // Fungsi untuk memperbarui pokemonList
    /*
    fun setPokemonList(newList: List<PokemonDetails>) {
        _pokemonList.value = newList
    }
*/
    fun getPokemonDetails(pokemonId: Int) {
        // Use viewModelScope to launch a coroutine for background operation
        viewModelScope.launch {
            try {
                // Call the repository method to get Pokemon details
                val pokemonDetails = repository.getPokemonDetails(pokemonId)

                // Update LiveData or perform other UI-related operations
                // based on the fetched Pokemon details
            } catch (e: Exception) {
                // Handle errors, such as network issues
            }
        }
    }

    // Add more methods as needed based on your requirements
}