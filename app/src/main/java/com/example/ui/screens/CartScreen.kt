package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.entities.CartItemEntity
import com.example.data.local.entities.CouponEntity
import com.example.data.local.entities.ProductEntity
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartItems: List<CartItemEntity>,
    productsMap: Map<String, ProductEntity>,
    appliedCoupon: CouponEntity?,
    couponMessage: String?,
    onUpdateQuantity: (CartItemEntity, Int) -> Unit,
    onRemoveItem: (String) -> Unit,
    onApplyCoupon: (String) -> Unit,
    onProceedToCheckout: () -> Unit
) {
    var couponInput by remember { mutableStateOf("") }
    var isGiftWrap by remember { mutableStateOf(false) }

    val subtotal = remember(cartItems, productsMap) {
        cartItems.sumOf { cartItem ->
            val product = productsMap[cartItem.productId]
            (product?.price ?: 0.0) * cartItem.quantity
        }
    }

    val discount = remember(subtotal, appliedCoupon) {
        if (appliedCoupon != null && subtotal >= appliedCoupon.minSpend) {
            subtotal * (appliedCoupon.discountPercent / 100.0)
        } else 0.0
    }

    val giftWrapFee = if (isGiftWrap) 5.0 else 0.0
    val deliveryFee = if (subtotal > 150.0 || cartItems.isEmpty()) 0.0 else 15.0
    val total = (subtotal - discount + deliveryFee + giftWrapFee).coerceAtLeast(0.0)

    if (cartItems.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.RemoveShoppingCart,
                    contentDescription = null,
                    tint = GoldAccent,
                    modifier = Modifier.size(72.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Your Shopping Cart is Empty",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Browse Mrs. Farhana Nadeem's Three Brothers collection to add luxury items!",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "My Cart (${cartItems.size} items)",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(cartItems) { cartItem ->
                    val product = productsMap[cartItem.productId]
                    if (product != null) {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = product.imageUri,
                                    contentDescription = product.title,
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(RoundedCornerShape(10.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = product.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "Variant: ${cartItem.selectedColor.ifEmpty { "Default" }} / ${cartItem.selectedSize.ifEmpty { "Standard" }}",
                                        fontSize = 10.sp,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "$${product.price}",
                                        fontWeight = FontWeight.ExtraBold,
                                        color = NavyPrimary,
                                        fontSize = 14.sp
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { onUpdateQuantity(cartItem, cartItem.quantity - 1) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Default.Remove, null)
                                    }
                                    Text(
                                        text = "${cartItem.quantity}",
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )
                                    IconButton(
                                        onClick = { onUpdateQuantity(cartItem, cartItem.quantity + 1) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Default.Add, null)
                                    }
                                    IconButton(
                                        onClick = { onRemoveItem(cartItem.id) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, null, tint = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    // Promo Coupon Section
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Coupon Code", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedTextField(
                                    value = couponInput,
                                    onValueChange = { couponInput = it },
                                    placeholder = { Text("e.g. FARHANA10 or 3BROTHERS20", fontSize = 11.sp) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp),
                                    shape = RoundedCornerShape(24.dp),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = { onApplyCoupon(couponInput) },
                                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
                                ) {
                                    Text("Apply", fontSize = 12.sp)
                                }
                            }
                            if (couponMessage != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = couponMessage,
                                    fontSize = 11.sp,
                                    color = if (appliedCoupon != null) Color(0xFF15803D) else Color.Red
                                )
                            }
                        }
                    }
                }

                item {
                    // Gift Wrapping Checkbox
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = isGiftWrap,
                            onCheckedChange = { isGiftWrap = it },
                            colors = CheckboxDefaults.colors(checkedColor = GoldAccent)
                        )
                        Text("Add Luxury Gift Wrapping ($5.00)", fontSize = 12.sp)
                    }
                }
            }

            // Summary Calculation Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SummaryRow("Subtotal", "$${String.format("%.2f", subtotal)}")
                    if (discount > 0) {
                        SummaryRow("Coupon Discount", "-$${String.format("%.2f", discount)}", isDiscount = true)
                    }
                    if (isGiftWrap) {
                        SummaryRow("Gift Wrap Fee", "$5.00")
                    }
                    SummaryRow(
                        "Express Delivery",
                        if (deliveryFee == 0.0) "FREE" else "$${String.format("%.2f", deliveryFee)}"
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Grand Total", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(
                            "$${String.format("%.2f", total)}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = NavyPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onProceedToCheckout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
                    ) {
                        Text("Proceed to Checkout", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String, isDiscount: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Text(
            value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDiscount) Color(0xFF15803D) else Color.Unspecified
        )
    }
}
