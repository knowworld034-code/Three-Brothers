package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val subcategory: String,
    val price: Double,
    val originalPrice: Double,
    val rating: Float,
    val reviewCount: Int,
    val imageUri: String,
    val description: String,
    val stock: Int,
    val isFlashSale: Boolean = false,
    val isBestSeller: Boolean = false,
    val isNewArrival: Boolean = false,
    val sellerName: String = "Three Brothers Official",
    val brand: String = "Three Brothers Premium",
    val specsJson: String = "{}",
    val hasVideo: Boolean = false
)

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val id: String, // productId_variant
    val productId: String,
    val quantity: Int,
    val selectedColor: String = "",
    val selectedSize: String = ""
)

@Entity(tableName = "wishlist_items")
data class WishlistItemEntity(
    @PrimaryKey val productId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val orderId: String,
    val timestamp: Long,
    val status: String, // PENDING, PROCESSING, SHIPPED, OUT_FOR_DELIVERY, DELIVERED, CANCELLED, RETURN_REQUESTED
    val totalPrice: Double,
    val paymentMethod: String,
    val itemsJson: String,
    val shippingAddress: String,
    val trackingNumber: String,
    val customerPhone: String = "+92 300 1234567"
)

@Entity(tableName = "addresses")
data class AddressEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fullName: String,
    val phone: String,
    val street: String,
    val city: String,
    val postalCode: String,
    val isDefault: Boolean = false
)

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: String,
    val userName: String,
    val userRating: Float,
    val comment: String,
    val date: String
)

@Entity(tableName = "coupons")
data class CouponEntity(
    @PrimaryKey val code: String,
    val discountPercent: Int,
    val minSpend: Double,
    val isActive: Boolean = true
)

@Entity(tableName = "wallet")
data class WalletEntity(
    @PrimaryKey val id: Int = 1,
    val balance: Double = 150.00,
    val rewardPoints: Int = 450,
    val referralCode: String = "FARHANA-3BRO"
)
