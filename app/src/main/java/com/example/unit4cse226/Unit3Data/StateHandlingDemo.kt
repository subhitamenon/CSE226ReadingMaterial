package com.example.unit4cse226.Unit3Data

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unit4cse226.Unit3Data.ui.theme.Unit4Cse226Theme

class StateHandlingDemo : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodCartScreen()
        }
    }
}
data class FoodItemData(
    val name: String,
    val price: Int,
    var quantity: Int
)
class FoodCartViewModel : ViewModel() {
    var foodItems = mutableStateListOf(
        FoodItemData("Paneer Pizza", 250, 1),
        FoodItemData("Burger", 150, 1),
        FoodItemData("Biryani", 200, 1))
        private set
    fun addItem(index: Int) {
        foodItems[index] = foodItems[index].copy(quantity = foodItems[index].quantity + 1)
    }
    fun removeItem(index: Int) {
        if (foodItems[index].quantity > 0) {
            foodItems[index] = foodItems[index].copy(
                quantity = foodItems[index].quantity - 1)
        }
    }
    fun totalBill(): Int {
        return foodItems.sumOf {
            it.price * it.quantity
        }
    }

}
@Composable
fun FoodCartScreen(viewModel: FoodCartViewModel = viewModel()) {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.Center) {
        Text("My Food Cart")
        Spacer(modifier = Modifier.height(20.dp))
        viewModel.foodItems.forEachIndexed { index, item ->
            FoodItem(
                name = item.name,
                price = item.price,
                quantity = item.quantity,
                onAddClick = {
                    viewModel.addItem(index) },
                onRemoveClick = {
                    viewModel.removeItem(index)
                })
            Spacer(modifier = Modifier.height(15.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Total Bill: ₹${viewModel.totalBill()}")
    }
}
@Composable
fun FoodItem(
    name: String,
    price: Int,
    quantity: Int,
    onAddClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(name)
            Text("$price")
        }
        Button(
            onClick = onRemoveClick
        ) {
            Text("-")
        }
        Text(
            text = "$quantity",
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Button(
            onClick = onAddClick
        ) {
            Text("+")
        }
    }}