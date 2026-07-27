package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.data.local.entities.AddressEntity
import com.example.data.local.entities.CartItemEntity
import com.example.data.local.entities.ProductEntity
import com.example.data.local.entities.WalletEntity
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartItems: List<CartItemEntity>,
    productsMap: Map<String, ProductEntity>,
    addresses: List<AddressEntity>,
    wallet: WalletEntity?,
    onBackClick: () -> Unit,
    onPlaceOrder: (paymentMethod: String, address: AddressEntity, totalPrice: Double) -> Unit
) {
    var selectedAddress by remember { mutableStateOf(addresses.firstOrNull() ?: AddressEntity(fullName = "Farhana Nadeem", phone = "+92 300 9876543", street = "House 45 Gulberg III", city = "Lahore", postalCode = "54000")) }
    var selectedPaymentMethod by remember { mutableStateOf("Cash on Delivery") }

    val subtotal = cartItems.sumOf { (productsMap[it.productId]?.price ?: 0.0) * it.quantity }
    val deliveryFee = if (subtotal > 150.0) 0.0 else 15.0
    val grandTotal = subtotal + deliveryFee

    var isOrderSuccessOpen by remember { mutableStateOf(false) }
    var generatedOrderId by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Secure Checkout", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 8.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total Amount", fontSize = 11.sp, color = Color.Gray)
                        Text(
                            "$${String.format("%.2f", grandTotal)}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = NavyPrimary
                            )
                        )
                    }

                    Button(
                        onClick = {
                            onPlaceOrder(selectedPaymentMethod, selectedAddress, grandTotal)
                            generatedOrderId = "TB-3B-${(100000..999999).random()}"
                            isOrderSuccessOpen = true
                        },
                        modifier = Modifier
                            .height(48.dp)
                            .width(180.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
                    ) {
                        Text("Place Order", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Shipping Address Card
            Text("Shipping Address", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(selectedAddress.fullName, fontWeight = FontWeight.Bold)
                        Text(selectedAddress.phone, fontSize = 11.sp, color = GoldAccent)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("${selectedAddress.street}, ${selectedAddress.city}", fontSize = 12.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Payment Methods
            Text("Select Payment Method", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))

            val paymentOptions = listOf(
                "Cash on Delivery" to Icons.Default.Payments,
                "QR Code Scan & Pay" to Icons.Default.QrCodeScanner,
                "Credit / Debit Card" to Icons.Default.CreditCard,
                "Three Brothers Wallet ($${wallet?.balance ?: 250.0})" to Icons.Default.AccountBalanceWallet
            )

            paymentOptions.forEach { (option, icon) ->
                Card(
                    onClick = { selectedPaymentMethod = option },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedPaymentMethod == option) GoldAccent.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
                    ),
                    border = if (selectedPaymentMethod == option) BorderStroke(1.5.dp, GoldAccent) else null
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedPaymentMethod == option,
                            onClick = { selectedPaymentMethod = option },
                            colors = RadioButtonDefaults.colors(selectedColor = GoldAccent)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(icon, contentDescription = null, tint = NavyPrimary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(option, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Delivery Time Estimation Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.LocalShipping, null, tint = GoldAccent, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Estimated Delivery: 1 - 2 Business Days", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Express courier dispatch via Three Brothers Logistics", fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }
        }
    }

    if (isOrderSuccessOpen) {
        AlertDialog(
            onDismissRequest = {
                isOrderSuccessOpen = false
                onBackClick()
            },
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF166534), modifier = Modifier.size(56.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Order Confirmed!", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Order ID: $generatedOrderId", fontWeight = FontWeight.ExtraBold, color = GoldAccent)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Thank you for shopping with Three Brothers! Mrs. Farhana Nadeem's team is preparing your package for dispatch.")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isOrderSuccessOpen = false
                        onBackClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
                ) {
                    Text("Track Order Progress")
                }
            }
        )
    }
}
