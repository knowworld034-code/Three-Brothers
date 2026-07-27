package com.example.data.repository

import com.example.data.InitialData
import com.example.data.local.dao.StoreDao
import com.example.data.local.entities.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class StoreRepository(private val dao: StoreDao) {

    val allProducts: Flow<List<ProductEntity>> = dao.getAllProducts()
    val cartItems: Flow<List<CartItemEntity>> = dao.getCartItems()
    val wishlistItems: Flow<List<WishlistItemEntity>> = dao.getWishlistItems()
    val allOrders: Flow<List<OrderEntity>> = dao.getAllOrders()
    val addresses: Flow<List<AddressEntity>> = dao.getAddresses()
    val coupons: Flow<List<CouponEntity>> = dao.getAllCoupons()
    val wallet: Flow<WalletEntity?> = dao.getWallet()

    suspend fun seedInitialDataIfNeeded() {
        val existingProducts = dao.getAllProducts().first()
        if (existingProducts.isEmpty()) {
            dao.insertProducts(InitialData.sampleProducts)
            dao.insertAddress(InitialData.sampleAddress)
            dao.updateWallet(InitialData.sampleWallet)
            InitialData.sampleCoupons.forEach { dao.insertCoupon(it) }
            InitialData.sampleReviews.forEach { dao.insertReview(it) }
        }
    }

    suspend fun getProductById(id: String): ProductEntity? = dao.getProductById(id)

    suspend fun addToCart(productId: String, color: String = "", size: String = "", quantity: Int = 1) {
        val cartItemId = "${productId}_${color}_${size}"
        dao.insertCartItem(
            CartItemEntity(
                id = cartItemId,
                productId = productId,
                quantity = quantity,
                selectedColor = color,
                selectedSize = size
            )
        )
    }

    suspend fun removeFromCart(cartItemId: String) {
        dao.deleteCartItem(cartItemId)
    }

    suspend fun clearCart() {
        dao.clearCart()
    }

    suspend fun toggleWishlist(productId: String) {
        val currentWishlist = dao.getWishlistItems().first()
        val exists = currentWishlist.any { it.productId == productId }
        if (exists) {
            dao.deleteWishlist(productId)
        } else {
            dao.insertWishlist(WishlistItemEntity(productId = productId))
        }
    }

    suspend fun placeOrder(
        items: List<CartItemEntity>,
        productsMap: Map<String, ProductEntity>,
        totalPrice: Double,
        paymentMethod: String,
        address: AddressEntity
    ): String {
        val orderId = "TB-3B-${System.currentTimeMillis().toString().takeLast(6)}"
        val itemsSummary = items.joinToString("; ") { cartItem ->
            val p = productsMap[cartItem.productId]
            "${p?.title ?: "Item"} x${cartItem.quantity} ($${p?.price})"
        }
        val addressString = "${address.fullName}, ${address.street}, ${address.city} (${address.phone})"

        val newOrder = OrderEntity(
            orderId = orderId,
            timestamp = System.currentTimeMillis(),
            status = "PROCESSING",
            totalPrice = totalPrice,
            paymentMethod = paymentMethod,
            itemsJson = itemsSummary,
            shippingAddress = addressString,
            trackingNumber = "TRK-${(100000..999999).random()}"
        )

        dao.insertOrder(newOrder)
        dao.clearCart()
        return orderId
    }

    suspend fun updateOrderStatus(orderId: String, newStatus: String) {
        dao.updateOrderStatus(orderId, newStatus)
    }

    suspend fun validateCoupon(code: String): CouponEntity? {
        return dao.getCouponByCode(code.trim().uppercase())
    }

    suspend fun addProduct(product: ProductEntity) {
        dao.insertProduct(product)
    }

    suspend fun deleteProduct(product: ProductEntity) {
        dao.deleteProduct(product)
    }

    fun getReviewsForProduct(productId: String): Flow<List<ReviewEntity>> = dao.getReviewsForProduct(productId)

    suspend fun addReview(review: ReviewEntity) {
        dao.insertReview(review)
    }

    suspend fun updateWallet(wallet: WalletEntity) {
        dao.updateWallet(wallet)
    }
}
