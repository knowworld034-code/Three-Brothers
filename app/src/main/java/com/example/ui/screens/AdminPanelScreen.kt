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
import com.example.data.local.entities.OrderEntity
import com.example.data.local.entities.ProductEntity
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    products: List<ProductEntity>,
    orders: List<OrderEntity>,
    onAddProduct: (title: String, category: String, price: Double, stock: Int, description: String, seller: String) -> Unit,
    onUpdateOrderStatus: (orderId: String, newStatus: String) -> Unit
) {
    var titleInput by remember { mutableStateOf("") }
    var categoryInput by remember { mutableStateOf("Watches & Jewelry") }
    var priceInput by remember { mutableStateOf("") }
    var stockInput by remember { mutableStateOf("") }
    var descriptionInput by remember { mutableStateOf("") }
    var sellerInput by remember { mutableStateOf("Three Brothers Official") }

    var isAddSuccessModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 80.dp)
    ) {
        Text("Admin Store Management", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
        Text("Owner: Mrs. Farhana Nadeem", fontSize = 12.sp, color = GoldAccent)

        Spacer(modifier = Modifier.height(16.dp))

        // Analytics Row Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricCard("Total Revenue", "$18,450.00", Icons.Default.AttachMoney, Modifier.weight(1f))
            MetricCard("Total Orders", "${orders.size + 24}", Icons.Default.ShoppingBag, Modifier.weight(1f))
            MetricCard("Products", "${products.size}", Icons.Default.Inventory, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Add Product Form Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Add New Product to Store Catalog", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = titleInput,
                    onValueChange = { titleInput = it },
                    label = { Text("Product Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = categoryInput,
                        onValueChange = { categoryInput = it },
                        label = { Text("Category") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = priceInput,
                        onValueChange = { priceInput = it },
                        label = { Text("Price ($)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = stockInput,
                        onValueChange = { stockInput = it },
                        label = { Text("Stock Qty") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = sellerInput,
                        onValueChange = { sellerInput = it },
                        label = { Text("Seller Name") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = descriptionInput,
                    onValueChange = { descriptionInput = it },
                    label = { Text("Description & Specs") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (titleInput.isNotBlank() && priceInput.isNotBlank()) {
                            val price = priceInput.toDoubleOrNull() ?: 99.99
                            val stock = stockInput.toIntOrNull() ?: 10
                            onAddProduct(titleInput, categoryInput, price, stock, descriptionInput, sellerInput)
                            isAddSuccessModal = true
                            titleInput = ""
                            priceInput = ""
                            stockInput = ""
                            descriptionInput = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
                ) {
                    Icon(Icons.Default.Add, null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Publish Product to App")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Order Management & Status Updater
        Text("Manage Customer Orders", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))

        orders.forEach { order ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Order #${order.orderId}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Status: ${order.status}", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                    Text("Total: $${order.totalPrice} | Customer: ${order.customerPhone}", fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("PROCESSING", "SHIPPED", "OUT_FOR_DELIVERY", "DELIVERED").forEach { status ->
                            FilterChip(
                                selected = order.status == status,
                                onClick = { onUpdateOrderStatus(order.orderId, status) },
                                label = { Text(status.take(6), fontSize = 9.sp) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (isAddSuccessModal) {
        AlertDialog(
            onDismissRequest = { isAddSuccessModal = false },
            title = { Text("Product Added!") },
            text = { Text("The new product has been successfully created and published to the Three Brothers app catalog.") },
            confirmButton = {
                Button(onClick = { isAddSuccessModal = false }) { Text("OK") }
            }
        )
    }
}

@Composable
fun MetricCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = NavyPrimary),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Icon(icon, null, tint = GoldAccent, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, color = Color.LightGray, fontSize = 9.sp)
            Text(value, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
        }
    }
}
