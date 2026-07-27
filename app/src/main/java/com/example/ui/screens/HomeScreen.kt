package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.local.entities.ProductEntity
import com.example.data.local.entities.WalletEntity
import com.example.ui.components.ProductCard
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldBackground
import com.example.ui.theme.NavyPrimary
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    products: List<ProductEntity>,
    wishlistProductIds: Set<String>,
    compareProductIds: Set<String>,
    wallet: WalletEntity?,
    selectedCategory: String,
    searchQuery: String,
    onCategorySelect: (String) -> Unit,
    onProductSelect: (ProductEntity) -> Unit,
    onAddToCart: (ProductEntity) -> Unit,
    onToggleWishlist: (String) -> Unit,
    onToggleCompare: (ProductEntity) -> Unit,
    onOpenAiAssistant: () -> Unit,
    onOpenQrPay: () -> Unit,
    onOpenLiveChat: () -> Unit
) {
    val context = LocalContext.current

    // Filter products by search and category
    val filteredProducts = remember(products, selectedCategory, searchQuery) {
        products.filter { p ->
            val matchesCategory = (selectedCategory == "All" || p.category.equals(selectedCategory, ignoreCase = true))
            val matchesSearch = searchQuery.isBlank() ||
                    p.title.contains(searchQuery, ignoreCase = true) ||
                    p.category.contains(searchQuery, ignoreCase = true) ||
                    p.description.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    val categories = listOf("All", "Watches & Jewelry", "Women's Wear", "Electronics", "Fragrances", "Men's Fashion", "Home & Living")

    // Flash sale timer state
    var secondsLeft by remember { mutableIntStateOf(14320) }
    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
    }

    val hours = secondsLeft / 3600
    val minutes = (secondsLeft % 3600) / 60
    val seconds = secondsLeft % 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        // Owner Greeting & Store Welcome Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = NavyPrimary)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Owner avatar image
                AsyncImage(
                    model = R.drawable.img_store_owner_1784846947303,
                    contentDescription = "Mrs. Farhana Nadeem",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(2.dp, GoldAccent, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Mrs. Farhana Nadeem Welcomes You",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "Owner & Curator of Three Brothers",
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldAccent
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "100% Certified Original Brands • Express Delivery • Cash on Delivery Available",
                        fontSize = 11.sp,
                        color = Color.LightGray
                    )
                }
            }
        }

        // Promotional Banner Carousel Slider
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = R.drawable.img_hero_banner_1_1784846910017,
                    contentDescription = "Banner Offer",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.7f), Color.Transparent)
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(20.dp)
                ) {
                    Text(
                        text = "ROYAL GOLD COLLECTION",
                        color = GoldAccent,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Up to 40% OFF\nLuxury Watches & Jewelry",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { onCategorySelect("Watches & Jewelry") },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text("Shop Collection", fontSize = 12.sp, color = Color.White)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Actions Row (Loyalty Points, QR Pay, WhatsApp, AI Concierge)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            QuickActionButton(
                icon = Icons.Default.Stars,
                label = "${wallet?.rewardPoints ?: 850} Pts",
                subtitle = "Loyalty Rewards",
                onClick = {}
            )
            QuickActionButton(
                icon = Icons.Default.QrCodeScanner,
                label = "Scan & Pay",
                subtitle = "QR Payments",
                onClick = onOpenQrPay
            )
            QuickActionButton(
                icon = Icons.Default.SupportAgent,
                label = "WhatsApp",
                subtitle = "24/7 Support",
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/923001234567?text=Hello%20Three%20Brothers%20Support"))
                    context.startActivity(intent)
                }
            )
            QuickActionButton(
                icon = Icons.Default.AutoAwesome,
                label = "AI Assistant",
                subtitle = "Smart Shopping",
                onClick = onOpenAiAssistant
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Categories Chips
        Text(
            text = "Categories",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                val isSelected = selectedCategory == category
                FilterChip(
                    selected = isSelected,
                    onClick = { onCategorySelect(category) },
                    label = { Text(category, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NavyPrimary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Flash Sales Section with Countdown Timer
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = GoldBackground),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Flash Sale",
                        tint = GoldAccent,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "FLASH SALE DEALS",
                            fontWeight = FontWeight.ExtraBold,
                            color = NavyPrimary,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Limited stock offers",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Timer Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(NavyPrimary)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = String.format("%02dh : %02dm : %02ds", hours, minutes, seconds),
                        color = GoldAccent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Flash Sale Horizontal List
        val flashSaleProducts = filteredProducts.filter { it.isFlashSale }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(flashSaleProducts) { product ->
                ProductCard(
                    product = product,
                    isInWishlist = wishlistProductIds.contains(product.id),
                    isInCompare = compareProductIds.contains(product.id),
                    onProductClick = { onProductSelect(product) },
                    onAddToCartClick = { onAddToCart(product) },
                    onWishlistToggle = { onToggleWishlist(product.id) },
                    onCompareToggle = { onToggleCompare(product) },
                    modifier = Modifier.width(170.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Best Sellers & All Products Grid
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Featured Products (${filteredProducts.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "View All",
                color = GoldAccent,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onCategorySelect("All") }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            val chunked = filteredProducts.chunked(2)
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
    }
}

@Composable
fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(82.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = GoldAccent,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Text(
                text = subtitle,
                fontSize = 9.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
