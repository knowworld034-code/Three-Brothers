package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.ProductEntity
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyPrimary

@Composable
fun SellerPanelScreen(
    products: List<ProductEntity>,
    onAddProductClick: () -> Unit,
    onDeleteProduct: (ProductEntity) -> Unit
) {
    var isWithdrawOpen by remember { mutableStateOf(false) }
    var withdrawAmount by remember { mutableStateOf("") }
    var payoutMessage by remember { mutableStateOf<String?>(null) }

    val sellerProducts = products.filter { it.sellerName.contains("Three Brothers", ignoreCase = true) || it.sellerName.contains("Farhana", ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 80.dp)
    ) {
        Text("Seller Portal Dashboard", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
        Text("Verified Store: Farhana Couture & Three Brothers", fontSize = 12.sp, color = GoldAccent)

        Spacer(modifier = Modifier.height(16.dp))

        // Earnings Summary
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NavyPrimary)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Total Net Earnings", color = Color.LightGray, fontSize = 11.sp)
                Text("$4,820.50", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = GoldAccent))

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Available Payout: $1,250.00", color = Color.White, fontSize = 12.sp)
                        Text("Commission Rate: 5%", color = Color.LightGray, fontSize = 10.sp)
                    }

                    Button(
                        onClick = { isWithdrawOpen = true },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
                    ) {
                        Text("Request Payout", fontSize = 11.sp, color = Color.White)
                    }
                }
            }
        }

        if (payoutMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(payoutMessage!!, color = Color(0xFF15803D), fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Seller Catalog
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("My Product Listing (${sellerProducts.size})", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        sellerProducts.forEach { product ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(product.title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Price: $${product.price} | Stock: ${product.stock} units", fontSize = 11.sp, color = Color.Gray)
                    }

                    IconButton(onClick = { onDeleteProduct(product) }) {
                        Icon(Icons.Default.Delete, null, tint = Color.Red)
                    }
                }
            }
        }
    }

    if (isWithdrawOpen) {
        AlertDialog(
            onDismissRequest = { isWithdrawOpen = false },
            title = { Text("Request Earnings Withdrawal") },
            text = {
                Column {
                    Text("Available for payout: $1,250.00")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = withdrawAmount,
                        onValueChange = { withdrawAmount = it },
                        label = { Text("Amount ($)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isWithdrawOpen = false
                        payoutMessage = "Payout request for $$withdrawAmount submitted to Mrs. Farhana Nadeem's accounts team!"
                        withdrawAmount = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
                ) {
                    Text("Confirm Payout")
                }
            },
            dismissButton = {
                TextButton(onClick = { isWithdrawOpen = false }) { Text("Cancel") }
            }
        )
    }
}
