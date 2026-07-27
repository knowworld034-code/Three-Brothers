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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.entities.ProductEntity
import com.example.ui.components.ProductCard
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyPrimary

@Composable
fun WishlistAndCompareScreen(
    wishlistProducts: List<ProductEntity>,
    compareProducts: List<ProductEntity>,
    wishlistProductIds: Set<String>,
    compareProductIds: Set<String>,
    onProductSelect: (ProductEntity) -> Unit,
    onAddToCart: (ProductEntity) -> Unit,
    onToggleWishlist: (String) -> Unit,
    onToggleCompare: (ProductEntity) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 80.dp)
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = GoldAccent
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Wishlist (${wishlistProducts.size})", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Favorite, null) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Compare (${compareProducts.size})", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.CompareArrows, null) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == 0) {
            if (wishlistProducts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.FavoriteBorder, null, tint = GoldAccent, modifier = Modifier.size(56.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Your Wishlist is empty", fontWeight = FontWeight.Bold)
                        Text("Tap the heart icon on any product to save it here!", fontSize = 11.sp, color = Color.Gray)
                    }
                }
            } else {
                val chunked = wishlistProducts.chunked(2)
                chunked.forEach { rowProducts ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowProducts.forEach { product ->
                            ProductCard(
                                product = product,
                                isInWishlist = wishlistProductIds.contains(product.id),
                                isInCompare = compareProductIds.contains(product.id),
                                onProductClick = { onProductSelect(product) },
                                onAddToCartClick = { onAddToCart(product) },
                                onWishlistToggle = { onToggleWishlist(product.id) },
                                onCompareToggle = { onToggleCompare(product) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowProducts.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        } else {
            // Side by Side Comparison Grid
            if (compareProducts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Compare, null, tint = GoldAccent, modifier = Modifier.size(56.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No products selected for comparison", fontWeight = FontWeight.Bold)
                        Text("Select up to 3 products to compare specs side by side!", fontSize = 11.sp, color = Color.Gray)
                    }
                }
            } else {
                Text("Side-by-Side Product Comparison", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    compareProducts.forEach { product ->
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                AsyncImage(
                                    model = product.imageUri,
                                    contentDescription = product.title,
                                    modifier = Modifier
                                        .height(100.dp)
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(product.title, fontWeight = FontWeight.Bold, fontSize = 11.sp, maxLines = 2)
                                Text("$${product.price}", color = NavyPrimary, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                                Text("Rating: ${product.rating}★", fontSize = 10.sp, color = GoldAccent)
                                Text("Seller: ${product.sellerName}", fontSize = 9.sp, color = Color.Gray)
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { onAddToCart(product) },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
                                ) {
                                    Text("Add", fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
