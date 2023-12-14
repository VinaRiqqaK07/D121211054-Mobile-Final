package model

data class PokemonDetails(
    val id: Int,
    val name: String,
    val baseExperience: Int,
    val height: Int,
    val weight: Int,
    val sprites: PokemonSprites
    // ...
)

data class PokemonSprites(
    val frontDefault: String,
    val frontFemale: String,
    val frontShiny: String,
    val frontShinyFemale: String
    // ...
)
