package com.D121211054.pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.D121211054.pokemon.network.ApiService
import com.D121211054.pokemon.repository.PokemonRepository
import com.D121211054.pokemon.ui.PokemonViewModel
import com.D121211054.pokemon.ui.theme.TestMobileFinalTheme
import com.D121211054.pokemon.ui.theme.Typography
import model.PokemonDetails
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    private val viewModel: PokemonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            TestMobileFinalTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    MainScreen(navController = navController)
                }
            }
        }
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun MyApp(content: @Composable () -> Unit){
    MaterialTheme{
        content()
    }
}

@Composable
fun PokemonListScreen(
    viewModel: PokemonViewModel,
    navController: NavController
){
    val pokemonList by remember { mutableStateOf(viewModel.pokemonList) }


    LazyColumn {
        items(pokemonList) { pokemon ->
            PokemonCard(
                pokemon = pokemon,
                onClick = { selectedPokemon ->
                    navController.navigate("pokemonDetail/${selectedPokemon.name}")
                }
            )
        }
    }
}

@Composable
fun PokemonCard(pokemon: PokemonDetails, onClick: (PokemonDetails) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(pokemon) },
        //elevation = 8.dp,
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = pokemon.name, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))

            if(pokemon.sprites.frontDefault.isNotEmpty()){
                Image(
                    painter = rememberImagePainter(data =  pokemon.sprites.frontDefault),
                    contentDescription = null,
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                )
            }

        }
    }
}

@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    onBackClicked: () -> Unit,
    viewModel: PokemonViewModel,
){

    val pokemonDetails = remember { mutableStateOf<PokemonDetails?>(null) }
    LaunchedEffect(pokemonId){
        val details = viewModel.getPokemonDetails(pokemonId)
        /*
        val coroutineScope = rememberCoroutineScope()
        coroutineScope.launch {
            val details = viewModel.getPokemonDetails(pokemonId)
            pokemonDetails.value = details
        }*/
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        pokemonDetails.value?.let { pokemon ->
        Text(
            text = "Pokemon Details",
            style = Typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(text = "Name: ${pokemon.name}", style = Typography.bodyMedium)
        Text(text = "Height: ${pokemon.height}", style = Typography.bodyMedium)
        Text(text = "Weight: ${pokemon.weight}", style = Typography.bodyMedium)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {onBackClicked()},
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Back")
        }
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)
    val repository = PokemonRepository(apiService)
    val viewModel: PokemonViewModel = remember { PokemonViewModel(repository) }
    NavHost(navController = navController, startDestination = "pokemonList"){
        composable("pokemonList"){
            PokemonListScreen(viewModel = viewModel, navController = navController)
        }
        composable("pokemonDetail/{pokemonId}"){ backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getString("pokemonId")?.toIntOrNull()
            pokemonId?.let {
                PokemonDetailScreen(pokemonId = it, onBackClicked = { navController.popBackStack() }, viewModel= viewModel)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonListScreenPreview() {
    TestMobileFinalTheme {

        val navController = rememberNavController()
        MainScreen(navController = navController)

        //Greeting("Android")
    }
}