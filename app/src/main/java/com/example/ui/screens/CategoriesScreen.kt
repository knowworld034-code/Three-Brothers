package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.ui.components.ProductCard
import com.example.ui.theme.GoldAccent

@Composable
fun CategoriesScreen(
    products: List<ProductEntity>,
    selectedCategory: String,
    sortOption: String,
    wishlistProductIds: Set<String>,
    compareProductIds: Set<String>,
    onCategorySelect: (String) -> Unit,
    onSortSelect: (String) -> Unit,
    onProductSelect: (ProductEntity) -> Unit,
    onAddToCart: (ProductEntity) -> Unit,
    onToggleWishlist: (String) -> Unit,
    onToggleCompare: (ProductEntity) -> Unit
) {
    val categories = listOf("All", "Watches & Jewelry", "Women's Wear", "Electronics", "Fragrances", "Men's Fashion", "Home & Living")
    val sortOptions = listOf("POPULAR", "PRICE_LOW_HIGH", "PRICE_HIGH_LOW", "RATING", "NEWEST")

    val sortedAndFiltered = remember(products, selectedCategory, sortOption) {
        val filtered = if (selectedCategory == "All") products else products.filter { it.category.equals(selectedCategory, ignoreCase = true) }
        when (sortOption) {
            "PRICE_LOW_HIGH" -> filtered.sortedBy { it.price }
            "PRICE_HIGH_LOW" -> filtered.sortedByDescending { it.price }
            "RATING" -> filtered.sortedByDescending { it.rating }
            "NEWEST" -> filtered.sortedByDescending { it.isNewArrival }
            else -> filtered.sortedByDescending { it.reviewCount }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 80.dp)
    ) {
        Text("Product Categories", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))

        Spacer(modifier = Modifier.height(12.dp))

        // Categories Chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories) { category ->
                val isSelected = selectedCategory == category
                FilterChip(
                    selected = isSelected,
                    onClick = { onCategorySelect(category) },
                    label = { Text(category) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GoldAccent,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Sorting Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Sort By:", fontSize = 12.sp, fontWeight = FontWeight.Bold)

            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(sortOptions) { option ->
                    FilterChip(
                        selected = sortOption == option,
                        onClick = { onSortSelect(option) },
                        label = {
                            Text(
                                when (option) {
                                    "PRICE_LOW_HIGH" -> "Price: Low to High"
                                    "PRICE_HIGH_LOW" -> "Price: High to Low"
                                    "RATING" -> "Top Rated"
                                    "NEWEST" -> "New Arrivals"
                                    else -> "Popularity"
                                },
                                fontSize = 11.sp
                            )
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Product Grid
        val chunked = sortedAndFiltered.chunked(2)
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
