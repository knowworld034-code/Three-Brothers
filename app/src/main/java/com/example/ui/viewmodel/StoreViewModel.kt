package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.ThreeBrothersAiHelper
import com.example.data.local.AppDatabase
import com.example.data.local.entities.*
import com.example.data.repository.StoreRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class UserRole {
    CUSTOMER, ADMIN, SELLER
}

enum class Language(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    URDU("ur", "اردو (Urdu)"),
    HINDI("hi", "हिंदी (Hindi)")
}

data class UiState(
    val role: UserRole = UserRole.CUSTOMER,
    val language: Language = Language.ENGLISH,
    val isDarkMode: Boolean = false,
    val selectedCategory: String = "All",
    val searchQuery: String = "",
    val sortOption: String = "POPULAR",
    val selectedProduct: ProductEntity? = null,
    val compareList: List<ProductEntity> = emptyList(),
    val appliedCoupon: CouponEntity? = null,
    val couponMessage: String? = null,
    val aiQuery: String = "",
    val aiResponse: String? = null,
    val isAiLoading: Boolean = false,
    val isLoggedIn: Boolean = true,
    val userName: String = "Farhana Nadeem",
    val userEmail: String = "farhana.nadeem@3brothers.shop",
    val userPhone: String = "+92 300 9876543",
    val activeTrackingOrder: OrderEntity? = null,
    val isQrPaymentOpen: Boolean = false,
    val isLiveChatOpen: Boolean = false,
    val selectedLanguageDialog: Boolean = false
)

class StoreViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: StoreRepository

    val products: StateFlow<List<ProductEntity>>
    val cartItems: StateFlow<List<CartItemEntity>>
    val wishlistItems: StateFlow<List<WishlistItemEntity>>
    val orders: StateFlow<List<OrderEntity>>
    val addresses: StateFlow<List<AddressEntity>>
    val wallet: StateFlow<WalletEntity?>

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        val dao = AppDatabase.getDatabase(application).storeDao()
        repository = StoreRepository(dao)

        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
        }

        products = repository.allProducts.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
        )
        cartItems = repository.cartItems.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
        )
        wishlistItems = repository.wishlistItems.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
        )
        orders = repository.allOrders.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
        )
        addresses = repository.addresses.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
        )
        wallet = repository.wallet.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), null
        )
    }

    fun setRole(role: UserRole) {
        _uiState.update { it.copy(role = role) }
    }

    fun setLanguage(language: Language) {
        _uiState.update { it.copy(language = language, selectedLanguageDialog = false) }
    }

    fun toggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun setSelectedCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setSortOption(sort: String) {
        _uiState.update { it.copy(sortOption = sort) }
    }

    fun selectProduct(product: ProductEntity?) {
        _uiState.update { it.copy(selectedProduct = product) }
    }

    fun addToCart(product: ProductEntity, color: String = "", size: String = "", quantity: Int = 1) {
        viewModelScope.launch {
            repository.addToCart(product.id, color, size, quantity)
        }
    }

    fun removeFromCart(cartItemId: String) {
        viewModelScope.launch {
            repository.removeFromCart(cartItemId)
        }
    }

    fun toggleWishlist(productId: String) {
        viewModelScope.launch {
            repository.toggleWishlist(productId)
        }
    }

    fun toggleCompare(product: ProductEntity) {
        _uiState.update { state ->
            val current = state.compareList.toMutableList()
            if (current.any { it.id == product.id }) {
                current.removeAll { it.id == product.id }
            } else if (current.size < 3) {
                current.add(product)
            }
            state.copy(compareList = current)
        }
    }

    fun applyCoupon(code: String) {
        viewModelScope.launch {
            val coupon = repository.validateCoupon(code)
            if (coupon != null) {
                _uiState.update { it.copy(appliedCoupon = coupon, couponMessage = "Coupon '${coupon.code}' Applied! ${coupon.discountPercent}% OFF") }
            } else {
                _uiState.update { it.copy(appliedCoupon = null, couponMessage = "Invalid or expired coupon code.") }
            }
        }
    }

    fun placeOrder(paymentMethod: String, address: AddressEntity, totalPrice: Double, onOrderPlaced: (String) -> Unit) {
        viewModelScope.launch {
            val currentCart = cartItems.value
            val currentProductsMap = products.value.associateBy { it.id }
            val orderId = repository.placeOrder(currentCart, currentProductsMap, totalPrice, paymentMethod, address)
            
            // Add loyalty points
            val currentWallet = wallet.value
            if (currentWallet != null) {
                val earnedPoints = (totalPrice * 2).toInt()
                repository.updateWallet(currentWallet.copy(rewardPoints = currentWallet.rewardPoints + earnedPoints))
            }
            
            _uiState.update { it.copy(appliedCoupon = null, couponMessage = null) }
            onOrderPlaced(orderId)
        }
    }

    fun updateOrderStatus(orderId: String, newStatus: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, newStatus)
        }
    }

    fun askAiAssistant(query: String) {
        if (query.isBlank()) return
        _uiState.update { it.copy(aiQuery = query, isAiLoading = true) }
        viewModelScope.launch {
            val catalogSummary = products.value.joinToString("\n") { 
                "${it.title} - $${it.price} (${it.category})" 
            }
            val answer = ThreeBrothersAiHelper.getAiRecommendation(query, catalogSummary)
            _uiState.update { it.copy(aiResponse = answer, isAiLoading = false) }
        }
    }

    fun addProductByAdminOrSeller(
        title: String,
        category: String,
        price: Double,
        stock: Int,
        description: String,
        sellerName: String
    ) {
        viewModelScope.launch {
            val newProduct = ProductEntity(
                id = "p_${System.currentTimeMillis()}",
                title = title,
                category = category,
                subcategory = "General",
                price = price,
                originalPrice = price * 1.25,
                rating = 5.0f,
                reviewCount = 1,
                imageUri = "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop&q=60",
                description = description,
                stock = stock,
                sellerName = sellerName
            )
            repository.addProduct(newProduct)
        }
    }

    fun addProductReview(productId: String, rating: Float, comment: String) {
        viewModelScope.launch {
            repository.addReview(
                ReviewEntity(
                    productId = productId,
                    userName = uiState.value.userName,
                    userRating = rating,
                    comment = comment,
                    date = "2026-07-23"
                )
            )
        }
    }

    fun getReviewsForProduct(productId: String): Flow<List<ReviewEntity>> {
        return repository.getReviewsForProduct(productId)
    }

    fun setActiveOrderForTracking(order: OrderEntity?) {
        _uiState.update { it.copy(activeTrackingOrder = order) }
    }

    fun toggleQrPaymentModal(isOpen: Boolean) {
        _uiState.update { it.copy(isQrPaymentOpen = isOpen) }
    }

    fun toggleLiveChat(isOpen: Boolean) {
        _uiState.update { it.copy(isLiveChatOpen = isOpen) }
    }

    fun toggleLanguageDialog(isOpen: Boolean) {
        _uiState.update { it.copy(selectedLanguageDialog = isOpen) }
    }
}
