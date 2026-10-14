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
import androidx.lifecycle.viewmodel.compose.viewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class RepoPatternDemo : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeScreen()
        }
    }
}
data class RecipeResponse(
    val recipes: List<Recipe>)
data class Recipe(
    val name: String,
    val cuisine: String,
    val difficulty: String,
    val rating: Double
)
interface RecipeApi {
    @GET("recipes")
    suspend fun getRecipes(): RecipeResponse
}
class RecipeRepository {
    private val api = Retrofit.Builder()
        //https://dummyjson.com/recipes
        .baseUrl("https://dummyjson.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RecipeApi::class.java)
    suspend fun getRecipes(): List<Recipe> {
        return api.getRecipes().recipes
    }
}
class RecipeViewModel : ViewModel() {
    private val repository = RecipeRepository()
    var recipes by mutableStateOf<List<Recipe>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    fun loadRecipes() {
        isLoading = true
        CoroutineScope(
            Dispatchers.IO
        ).launch {
            val result = repository.getRecipes()
            withContext(
                Dispatchers.Main
            ) {
                recipes = result
                isLoading = false
            }
        }
    }
}
@Composable
fun RecipeScreen(
    viewModel: RecipeViewModel = viewModel()
) {

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

        if (viewModel.isLoading) {

            CircularProgressIndicator()

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {

                items(viewModel.recipes) { recipe ->

                    RecipeItem(recipe)
                }
            }
        }
    }
}
@Composable
fun RecipeItem(recipe: Recipe) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = recipe.name)
        Text(text = "Cuisine: ${recipe.cuisine}")
        Text(text = "Difficulty: ${recipe.difficulty}")
        Text(text = "Rating: ${recipe.rating}")
    }
}