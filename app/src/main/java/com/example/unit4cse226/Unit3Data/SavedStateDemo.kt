package com.example.unit4cse226.Unit3Data

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


class SavedStateDemo : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            callVar()
        }
    }
}


class RestoreState(
    private val savedStateVar: SavedStateHandle
) : ViewModel() {

    var count by mutableIntStateOf(
        savedStateVar["count"] ?: 0
    )
        private set

    fun incrVar() {
        count++
        savedStateVar["count"] = count
    }
}

@Composable
fun callVar(viewModel: RestoreState = viewModel()) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Count of the variable is ${viewModel.count}"
        )

        Button(
            onClick = { viewModel.incrVar() }
        ) {
            Text("Increment var")
        }
    }
}