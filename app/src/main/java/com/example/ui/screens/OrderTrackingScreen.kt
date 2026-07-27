package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.OrderEntity
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyPrimary

@Composable
fun OrderTrackingScreen(
    orders: List<OrderEntity>,
    onUpdateOrderStatus: (orderId: String, newStatus: String) -> Unit
) {
    val activeOrder = orders.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 80.dp)
    ) {
        Text("My Orders & Live Tracking", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))

        Spacer(modifier = Modifier.height(16.dp))

        if (activeOrder == null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.LocalShipping, null, tint = GoldAccent, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No Recent Orders Found", fontWeight = FontWeight.Bold)
                    Text("Place an order from Mrs. Farhana Nadeem's store to see real-time delivery tracking!", fontSize = 11.sp, color = Color.Gray)
                }
            }
        } else {
            // Live Step Tracker Progress
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyPrimary)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Order #${activeOrder.orderId}", fontWeight = FontWeight.Bold, color = Color.White)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(GoldAccent)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(activeOrder.status, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tracking #: ${activeOrder.trackingNumber}", color = GoldAccent, fontSize = 11.sp)
                    Text("Delivery Address: ${activeOrder.shippingAddress}", color = Color.LightGray, fontSize = 11.sp)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Step pipeline
                    val steps = listOf("ORDER PLACED", "PACKED", "SHIPPED", "OUT FOR DELIVERY", "DELIVERED")
                    val currentStepIndex = when (activeOrder.status) {
                        "PENDING" -> 0
                        "PROCESSING" -> 1
                        "SHIPPED" -> 2
                        "OUT_FOR_DELIVERY" -> 3
                        "DELIVERED" -> 4
                        else -> 1
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        steps.forEachIndexed { index, step ->
                            val isCompleted = index <= currentStepIndex
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(if (isCompleted) GoldAccent else Color.Gray),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isCompleted) {
                                        Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    }
                                }
                                Text(
                                    text = step.take(5),
                                    fontSize = 8.sp,
                                    color = if (isCompleted) Color.White else Color.Gray,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Delivery Rider Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(GoldAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.DirectionsBike, null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Express Driver: Tariq Mahmood", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Three Brothers Express Dispatch • Vehicle # LEA-492", fontSize = 11.sp, color = Color.Gray)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Call, null, tint = GoldAccent)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Order History List
            Text("Order History", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))

            orders.forEach { order ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("ID: ${order.orderId}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("$${String.format("%.2f", order.totalPrice)}", fontWeight = FontWeight.Bold, color = GoldAccent, fontSize = 12.sp)
                        }
                        Text(order.itemsJson, fontSize = 11.sp, color = Color.Gray, maxLines = 1)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Status: ${order.status}", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            TextButton(onClick = {}) {
                                Icon(Icons.Default.PictureAsPdf, null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Download PDF Invoice", fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
