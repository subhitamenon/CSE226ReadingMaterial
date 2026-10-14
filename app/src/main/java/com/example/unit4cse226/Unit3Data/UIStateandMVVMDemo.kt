package com.example.unit4cse226.Unit3Data

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel

import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class UIStateandMVVMDemo : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeScreenn()
        }
    }
}
data class RecipeUiState(
    val isLoading: Boolean = false,
    val recipes: List<Recipee> = emptyList(),
    val errorMessage: String? = null)

data class RecipeResponsee(
    val recipes: List<Recipee>)
data class Recipee(
    val name: String,
    val cuisine: String,
    val difficulty: String,
    val rating: Double
)
interface RecipeApii {
    @GET("recipes")
    suspend fun getRecipes(): RecipeResponsee
}
class RecipeRepositoryy {
    private val api = Retrofit.Builder()
        //https://dummyjson.com/recipes
        .baseUrl("https://dummyjson.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RecipeApii::class.java)
    suspend fun getRecipes(): List<Recipee> {
        return api.getRecipes().recipes
    }
}
class RecipeViewModell : ViewModel() {
    private val repository = RecipeRepositoryy()
//    var recipes by mutableStateOf<List<Recipe>>(emptyList())
//        private set
//    var isLoading by mutableStateOf(false)
//        private set


    var uiState by mutableStateOf(RecipeUiState())
        private set
//    fun loadRecipes() {
//        isLoading = true
//        kotlinx.coroutines.CoroutineScope(
//            kotlinx.coroutines.Dispatchers.IO
//        ).launch {
//            val result = repository.getRecipes()
//            kotlinx.coroutines.withContext(
//                kotlinx.coroutines.Dispatchers.Main
//            ) {
//                recipes = result
//                isLoading = false
//            }
//        }


    fun loadRecipes() {

        uiState = uiState.copy(
            isLoading = true,
            errorMessage = null)
        viewModelScope.launch {
            try {

                val result = repository.getRecipes()

                uiState = uiState.copy(
                    isLoading = false,
                    recipes = result
                )

            } catch (e: Exception) {

                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Failed to load recipes"
                )
            }
        }
    }
}
@Composable
fun RecipeScreenn(
    viewModel: RecipeViewModell = viewModel()
) {
    val state = viewModel.uiState
    LaunchedEffect(Unit) {
        viewModel.loadRecipes()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Food Recipes"
        )

        if (state.isLoading) {

            CircularProgressIndicator()

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {

                items(state.recipes) { recipe ->

                    RecipeItemm(recipe)
                }
            }
        }
    }
}
@Composable
fun RecipeItemm(recipe: Recipee) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = recipe.name)
        Text(text = "Cuisine: ${recipe.cuisine}")
        Text(text = "Difficulty: ${recipe.difficulty}")
        Text(text = "Rating: ${recipe.rating}")
    }
}

