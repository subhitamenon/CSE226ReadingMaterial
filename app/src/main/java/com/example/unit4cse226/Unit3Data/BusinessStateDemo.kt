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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


class BusinessStateDemo : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CartScreen()

        }
    }
}
data class CartItem(
    val name: String,
    val price: Double,
    val quantity: Int
)

data class CartBusinessState(
    val items: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val discount: Double = 0.0,
    val finalPrice: Double = 0.0,
    val amountOfDiscount:Double = 0.0,
    val nextDiscount:Int = 10
)

class CartViewModel : ViewModel() {

    var businessState by mutableStateOf(CartBusinessState())
        private set

    fun addItem(item: CartItem) {
        val updatedItems = businessState.items + item
        calculateCart(updatedItems)
    }

    fun removeItem(item: CartItem) {
        val updatedItems = businessState.items - item
        calculateCart(updatedItems)
    }

    private fun calculateCart(items: List<CartItem>) {

        val total = items.sumOf {
            it.price * it.quantity
        }

//        val discount = if (total >= 1000) {
//            total * 0.10
//        } else {
//            0.0
//        }
        val discount =when{
            total >= 2000 -> total * .20
            total >= 1500 -> total * .15
            total >= 1000 -> total * .10
            else -> 0.0
        }
        val nextDiscount =when{
            total < 1000 -> 10
            total < 1500 -> 15
            total < 2000 -> 20
            else -> 20
        }
        val finalPrice = total - discount

        businessState = businessState.copy(
            items = items,
            totalPrice = total,
            discount = discount,
            finalPrice = finalPrice,
            amountOfDiscount = discount,
            nextDiscount = nextDiscount
        )
    }
}

@Composable
fun CartScreen(
    viewModel: CartViewModel = viewModel()
) {

    val state = viewModel.businessState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {

        Text(
            text = "Shopping Cart"
        )

        Button(
            onClick = {
                viewModel.addItem(
                    CartItem(
                        name = "Pizza",
                        price = 500.0,
                        quantity = 1
                    )
                )
            }
        ) {
            Text("Add Pizza")
        }

        Button(
            onClick = {
                viewModel.addItem(
                    CartItem(
                        name = "Burger",
                        price = 300.0,
                        quantity = 1
                    )
                )
            }
        ) {
            Text("Add Burger")
        }

        Text(
            text = "Items: ${state.items.size}"
        )

        Text(
            text = "Total: ${state.totalPrice}"
        )

        Text(
            text = "Discount: ${state.discount}"
        )

        Text(
            text = "Final Price: ${state.finalPrice}"
        )
        Text(text=
            when{
                state.totalPrice < 1000 ->
                    " Add ${1000- state.totalPrice} more to get 10 % discount "
                state.totalPrice < 1500 ->
                    " Add ${1500- state.totalPrice} more to get 15 % discount "
                state.totalPrice < 2000 ->
                    " Add ${2000- state.totalPrice} more to get 20 % discount "
                else -> "You have reached to the max discount of today's deal"


        })
    }
}