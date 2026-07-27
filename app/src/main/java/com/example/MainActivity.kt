package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entities.ProductEntity
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.ThreeBrothersTheme
import com.example.ui.viewmodel.StoreViewModel
import com.example.ui.viewmodel.UserRole

enum class Screen {
    HOME, CATEGORIES, CART, WISHLIST, ORDERS, PROFILE, PRODUCT_DETAILS, CHECKOUT, ADMIN_PANEL, SELLER_PANEL
}

class MainActivity : ComponentActivity() {

    private val viewModel: StoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val products by viewModel.products.collectAsStateWithLifecycle()
            val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
            val wishlistItems by viewModel.wishlistItems.collectAsStateWithLifecycle()
            val orders by viewModel.orders.collectAsStateWithLifecycle()
            val addresses by viewModel.addresses.collectAsStateWithLifecycle()
            val wallet by viewModel.wallet.collectAsStateWithLifecycle()

            var currentScreen by remember { mutableStateOf(Screen.HOME) }

            val productsMap = remember(products) { products.associateBy { it.id } }
            val wishlistProductIds = remember(wishlistItems) { wishlistItems.map { it.productId }.toSet() }
            val wishlistProducts = remember(products, wishlistProductIds) { products.filter { wishlistProductIds.contains(it.id) } }
            val compareProductIds = remember(uiState.compareList) { uiState.compareList.map { it.id }.toSet() }

            ThreeBrothersTheme(darkTheme = uiState.isDarkMode) {
                Scaffold(
                    topBar = {
                        TopHeaderBar(
                            role = uiState.role,
                            language = uiState.language,
                            isDarkMode = uiState.isDarkMode,
                            searchQuery = uiState.searchQuery,
                            onSearchChange = { viewModel.setSearchQuery(it) },
                            onRoleChange = { role ->
                                viewModel.setRole(role)
                                currentScreen = when (role) {
                                    UserRole.ADMIN -> Screen.ADMIN_PANEL
                                    UserRole.SELLER -> Screen.SELLER_PANEL
                                    UserRole.CUSTOMER -> Screen.HOME
                                }
                            },
                            onLanguageClick = { viewModel.toggleLanguageDialog(true) },
                            onDarkModeToggle = { viewModel.toggleDarkMode() },
                            onVoiceSearchClick = { viewModel.setSearchQuery("Royal Watch") },
                            onBarcodeScanClick = { viewModel.toggleQrPaymentModal(true) },
                            onAiClick = { viewModel.askAiAssistant("Recommend top luxury products from Three Brothers") }
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 8.dp
                        ) {
                            NavigationBarItem(
                                selected = currentScreen == Screen.HOME,
                                onClick = { currentScreen = Screen.HOME },
                                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                label = { Text("Home", fontSize = 10.sp) },
                                colors = NavigationBarItemDefaults.colors(selectedIconColor = GoldAccent, selectedTextColor = GoldAccent)
                            )
                            NavigationBarItem(
                                selected = currentScreen == Screen.CATEGORIES,
                                onClick = { currentScreen = Screen.CATEGORIES },
                                icon = { Icon(Icons.Default.Category, contentDescription = "Categories") },
                                label = { Text("Catalog", fontSize = 10.sp) },
                                colors = NavigationBarItemDefaults.colors(selectedIconColor = GoldAccent, selectedTextColor = GoldAccent)
                            )
                            NavigationBarItem(
                                selected = currentScreen == Screen.CART,
                                onClick = { currentScreen = Screen.CART },
                                icon = {
                                    BadgedBox(
                                        badge = {
                                            if (cartItems.isNotEmpty()) {
                                                Badge(containerColor = GoldAccent) {
                                                    Text("${cartItems.sumOf { it.quantity }}")
                                                }
                                            }
                                        }
                                    ) {
                                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                                    }
                                },
                                label = { Text("Cart", fontSize = 10.sp) },
                                colors = NavigationBarItemDefaults.colors(selectedIconColor = GoldAccent, selectedTextColor = GoldAccent)
                            )
                            NavigationBarItem(
                                selected = currentScreen == Screen.WISHLIST,
                                onClick = { currentScreen = Screen.WISHLIST },
                                icon = { Icon(Icons.Default.Favorite, contentDescription = "Wishlist") },
                                label = { Text("Wishlist", fontSize = 10.sp) },
                                colors = NavigationBarItemDefaults.colors(selectedIconColor = GoldAccent, selectedTextColor = GoldAccent)
                            )
                            NavigationBarItem(
                                selected = currentScreen == Screen.ORDERS,
                                onClick = { currentScreen = Screen.ORDERS },
                                icon = { Icon(Icons.Default.LocalShipping, contentDescription = "Orders") },
                                label = { Text("Orders", fontSize = 10.sp) },
                                colors = NavigationBarItemDefaults.colors(selectedIconColor = GoldAccent, selectedTextColor = GoldAccent)
                            )
                            NavigationBarItem(
                                selected = currentScreen == Screen.PROFILE,
                                onClick = { currentScreen = Screen.PROFILE },
                                icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                                label = { Text("Profile", fontSize = 10.sp) },
                                colors = NavigationBarItemDefaults.colors(selectedIconColor = GoldAccent, selectedTextColor = GoldAccent)
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentScreen) {
                            Screen.HOME -> HomeScreen(
                                products = products,
                                wishlistProductIds = wishlistProductIds,
                                compareProductIds = compareProductIds,
                                wallet = wallet,
                                selectedCategory = uiState.selectedCategory,
                                searchQuery = uiState.searchQuery,
                                onCategorySelect = { cat ->
                                    viewModel.setSelectedCategory(cat)
                                    currentScreen = Screen.CATEGORIES
                                },
                                onProductSelect = { product ->
                                    viewModel.selectProduct(product)
                                    currentScreen = Screen.PRODUCT_DETAILS
                                },
                                onAddToCart = { product -> viewModel.addToCart(product) },
                                onToggleWishlist = { id -> viewModel.toggleWishlist(id) },
                                onToggleCompare = { p -> viewModel.toggleCompare(p) },
                                onOpenAiAssistant = { viewModel.askAiAssistant("What is the best seller today?") },
                                onOpenQrPay = { viewModel.toggleQrPaymentModal(true) },
                                onOpenLiveChat = { viewModel.toggleLiveChat(true) }
                            )

                            Screen.CATEGORIES -> CategoriesScreen(
                                products = products,
                                selectedCategory = uiState.selectedCategory,
                                sortOption = uiState.sortOption,
                                wishlistProductIds = wishlistProductIds,
                                compareProductIds = compareProductIds,
                                onCategorySelect = { viewModel.setSelectedCategory(it) },
                                onSortSelect = { viewModel.setSortOption(it) },
                                onProductSelect = { product ->
                                    viewModel.selectProduct(product)
                                    currentScreen = Screen.PRODUCT_DETAILS
                                },
                                onAddToCart = { product -> viewModel.addToCart(product) },
                                onToggleWishlist = { id -> viewModel.toggleWishlist(id) },
                                onToggleCompare = { p -> viewModel.toggleCompare(p) }
                            )

                            Screen.CART -> CartScreen(
                                cartItems = cartItems,
                                productsMap = productsMap,
                                appliedCoupon = uiState.appliedCoupon,
                                couponMessage = uiState.couponMessage,
                                onUpdateQuantity = { cartItem, qty ->
                                    if (qty <= 0) viewModel.removeFromCart(cartItem.id)
                                    else {
                                        val product = productsMap[cartItem.productId]
                                        if (product != null) viewModel.addToCart(product, cartItem.selectedColor, cartItem.selectedSize, qty)
                                    }
                                },
                                onRemoveItem = { viewModel.removeFromCart(it) },
                                onApplyCoupon = { viewModel.applyCoupon(it) },
                                onProceedToCheckout = { currentScreen = Screen.CHECKOUT }
                            )

                            Screen.CHECKOUT -> CheckoutScreen(
                                cartItems = cartItems,
                                productsMap = productsMap,
                                addresses = addresses,
                                wallet = wallet,
                                onBackClick = { currentScreen = Screen.CART },
                                onPlaceOrder = { method, addr, total ->
                                    viewModel.placeOrder(method, addr, total) { orderId ->
                                        currentScreen = Screen.ORDERS
                                    }
                                }
                            )

                            Screen.PRODUCT_DETAILS -> {
                                val selectedProduct = uiState.selectedProduct ?: products.firstOrNull()
                                if (selectedProduct != null) {
                                    val reviews by viewModel.getReviewsForProduct(selectedProduct.id).collectAsStateWithLifecycle(emptyList())
                                    ProductDetailsScreen(
                                        product = selectedProduct,
                                        reviews = reviews,
                                        isInWishlist = wishlistProductIds.contains(selectedProduct.id),
                                        isInCompare = compareProductIds.contains(selectedProduct.id),
                                        onBackClick = { currentScreen = Screen.HOME },
                                        onAddToCart = { c, s, q ->
                                            viewModel.addToCart(selectedProduct, c, s, q)
                                            currentScreen = Screen.CART
                                        },
                                        onBuyNow = { c, s, q ->
                                            viewModel.addToCart(selectedProduct, c, s, q)
                                            currentScreen = Screen.CHECKOUT
                                        },
                                        onToggleWishlist = { viewModel.toggleWishlist(selectedProduct.id) },
                                        onToggleCompare = { viewModel.toggleCompare(selectedProduct) },
                                        onAddReview = { r, c -> viewModel.addProductReview(selectedProduct.id, r, c) },
                                        onOpenLiveChat = { viewModel.toggleLiveChat(true) }
                                    )
                                }
                            }

                            Screen.WISHLIST -> WishlistAndCompareScreen(
                                wishlistProducts = wishlistProducts,
                                compareProducts = uiState.compareList,
                                wishlistProductIds = wishlistProductIds,
                                compareProductIds = compareProductIds,
                                onProductSelect = { product ->
                                    viewModel.selectProduct(product)
                                    currentScreen = Screen.PRODUCT_DETAILS
                                },
                                onAddToCart = { product -> viewModel.addToCart(product) },
                                onToggleWishlist = { id -> viewModel.toggleWishlist(id) },
                                onToggleCompare = { p -> viewModel.toggleCompare(p) }
                            )

                            Screen.ORDERS -> OrderTrackingScreen(
                                orders = orders,
                                onUpdateOrderStatus = { orderId, status ->
                                    viewModel.updateOrderStatus(orderId, status)
                                }
                            )

                            Screen.PROFILE -> ProfileAndExtraScreen(
                                userName = uiState.userName,
                                userEmail = uiState.userEmail,
                                userPhone = uiState.userPhone,
                                wallet = wallet,
                                language = uiState.language,
                                isDarkMode = uiState.isDarkMode,
                                onLanguageClick = { viewModel.toggleLanguageDialog(true) },
                                onDarkModeToggle = { viewModel.toggleDarkMode() },
                                onOpenAiAssistant = { viewModel.askAiAssistant("Provide a shopping overview") },
                                onOpenQrPay = { viewModel.toggleQrPaymentModal(true) }
                            )

                            Screen.ADMIN_PANEL -> AdminPanelScreen(
                                products = products,
                                orders = orders,
                                onAddProduct = { title, cat, price, stock, desc, seller ->
                                    viewModel.addProductByAdminOrSeller(title, cat, price, stock, desc, seller)
                                },
                                onUpdateOrderStatus = { orderId, status ->
                                    viewModel.updateOrderStatus(orderId, status)
                                }
                            )

                            Screen.SELLER_PANEL -> SellerPanelScreen(
                                products = products,
                                onAddProductClick = {},
                                onDeleteProduct = { product -> viewModel.addProductByAdminOrSeller("", "", 0.0, 0, "", "") }
                            )
                        }

                        // Dialog Modals
                        if (uiState.aiResponse != null || uiState.isAiLoading) {
                            AiAssistantDialog(
                                aiQuery = uiState.aiQuery,
                                aiResponse = uiState.aiResponse,
                                isLoading = uiState.isAiLoading,
                                onDismiss = { viewModel.askAiAssistant("") },
                                onAsk = { viewModel.askAiAssistant(it) }
                            )
                        }

                        if (uiState.isQrPaymentOpen) {
                            QrPaymentModal(onDismiss = { viewModel.toggleQrPaymentModal(false) })
                        }

                        if (uiState.selectedLanguageDialog) {
                            LanguageModal(
                                currentLanguage = uiState.language,
                                onDismiss = { viewModel.toggleLanguageDialog(false) },
                                onSelectLanguage = { viewModel.setLanguage(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}
