package com.example.data

import com.example.data.local.entities.AddressEntity
import com.example.data.local.entities.CouponEntity
import com.example.data.local.entities.ProductEntity
import com.example.data.local.entities.ReviewEntity
import com.example.data.local.entities.WalletEntity

object InitialData {
    val sampleProducts = listOf(
        ProductEntity(
            id = "p1",
            title = "Three Brothers Royal Gold Chronograph Watch",
            category = "Watches & Jewelry",
            subcategory = "Luxury Watches",
            price = 249.99,
            originalPrice = 320.00,
            rating = 4.9f,
            reviewCount = 128,
            imageUri = "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop&q=60",
            description = "Precision Swiss quartz movement with solid 18k gold-plated stainless steel casing. Sapphire glass crystal and water resistance up to 50m. Mrs. Farhana Nadeem Signature Edition.",
            stock = 15,
            isFlashSale = true,
            isBestSeller = true,
            isNewArrival = false,
            sellerName = "Three Brothers Official",
            brand = "Three Brothers Royal",
            hasVideo = true
        ),
        ProductEntity(
            id = "p2",
            title = "Empress Silk Embroidery Kurti - Farhana Collection",
            category = "Women's Wear",
            subcategory = "Designer Kurtis",
            price = 89.99,
            originalPrice = 120.00,
            rating = 4.8f,
            reviewCount = 94,
            imageUri = "https://images.unsplash.com/photo-1583391733956-3750e0ff4e8b?w=600&auto=format&fit=crop&q=60",
            description = "Pure raw silk handcrafted designer kurti with gold zardozi embroidery work. Elegant fit designed exclusively for festive and formal occasions.",
            stock = 25,
            isFlashSale = true,
            isBestSeller = true,
            isNewArrival = true,
            sellerName = "Farhana Couture",
            brand = "Farhana Nadeem Design",
            hasVideo = true
        ),
        ProductEntity(
            id = "p3",
            title = "Three Brothers Pro Noise-Cancelling Headphones",
            category = "Electronics",
            subcategory = "Audio Gear",
            price = 179.50,
            originalPrice = 220.00,
            rating = 4.7f,
            reviewCount = 210,
            imageUri = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=60",
            description = "Active Noise Cancellation (ANC) with Hi-Res spatial audio, custom 40mm beryllium drivers, and 40-hour battery life with fast Type-C charging.",
            stock = 30,
            isFlashSale = false,
            isBestSeller = true,
            isNewArrival = true,
            sellerName = "Three Brothers Tech",
            brand = "Three Brothers Audio"
        ),
        ProductEntity(
            id = "p4",
            title = "Oud Royal Signature Eau De Parfum (100ml)",
            category = "Fragrances",
            subcategory = "Attar & Perfumes",
            price = 110.00,
            originalPrice = 150.00,
            rating = 5.0f,
            reviewCount = 82,
            imageUri = "https://images.unsplash.com/photo-1523293182086-7651a899d37f?w=600&auto=format&fit=crop&q=60",
            description = "Rich Cambodi Oud infused with Damask Rose, Amber, and Vanilla. Long-lasting luxury projection designed for distinction.",
            stock = 18,
            isFlashSale = true,
            isBestSeller = false,
            isNewArrival = true,
            sellerName = "Three Brothers Perfumes",
            brand = "Royal Oud"
        ),
        ProductEntity(
            id = "p5",
            title = "Men's Italian Leather Sherwani Shoes",
            category = "Men's Fashion",
            subcategory = "Footwear",
            price = 129.00,
            originalPrice = 165.00,
            rating = 4.6f,
            reviewCount = 45,
            imageUri = "https://images.unsplash.com/photo-1549298916-b41d501d3772?w=600&auto=format&fit=crop&q=60",
            description = "Handcrafted genuine leather monk strap khussa shoes with cushioned memory foam arch support. Perfect for weddings and celebrations.",
            stock = 12,
            isFlashSale = false,
            isBestSeller = false,
            isNewArrival = true,
            sellerName = "Three Brothers Footwear",
            brand = "Three Brothers Leather"
        ),
        ProductEntity(
            id = "p6",
            title = "Smart Ceramic Express Espresso Maker",
            category = "Home & Living",
            subcategory = "Kitchen Appliances",
            price = 145.00,
            originalPrice = 190.00,
            rating = 4.9f,
            reviewCount = 67,
            imageUri = "https://images.unsplash.com/photo-1517668808822-9ebe02f2a6e8?w=600&auto=format&fit=crop&q=60",
            description = "15-bar Italian pump pressure with integrated milk frother. Bluetooth mobile app schedule for morning coffee.",
            stock = 8,
            isFlashSale = false,
            isBestSeller = true,
            isNewArrival = false,
            sellerName = "Three Brothers Home",
            brand = "Three Brothers Home"
        ),
        ProductEntity(
            id = "p7",
            title = "Ultra HD 4K Smart Android Vision TV 55\"",
            category = "Electronics",
            subcategory = "Televisions",
            price = 499.00,
            originalPrice = 650.00,
            rating = 4.8f,
            reviewCount = 150,
            imageUri = "https://images.unsplash.com/photo-1593784991095-a205069470b6?w=600&auto=format&fit=crop&q=60",
            description = "Quantum Dot LED display with Dolby Vision HDR10+, hands-free voice search control, and ultra slim metallic bezel design.",
            stock = 5,
            isFlashSale = true,
            isBestSeller = true,
            isNewArrival = false,
            sellerName = "Three Brothers Tech",
            brand = "Three Brothers Vision"
        ),
        ProductEntity(
            id = "p8",
            title = "Emerald Cut Zirconia Gold Bridal Jewelry Set",
            category = "Watches & Jewelry",
            subcategory = "Bridal Sets",
            price = 299.99,
            originalPrice = 400.00,
            rating = 4.9f,
            reviewCount = 58,
            imageUri = "https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=600&auto=format&fit=crop&q=60",
            description = "Full necklace, matching earrings, and maang tikka set plated in 22k gold with hand-cut emerald green crystals.",
            stock = 10,
            isFlashSale = false,
            isBestSeller = true,
            isNewArrival = true,
            sellerName = "Farhana Jewels",
            brand = "Farhana Signature"
        )
    )

    val sampleCoupons = listOf(
        CouponEntity(code = "FARHANA10", discountPercent = 10, minSpend = 50.0, isActive = true),
        CouponEntity(code = "3BROTHERS20", discountPercent = 20, minSpend = 100.0, isActive = true),
        CouponEntity(code = "WELCOME50", discountPercent = 15, minSpend = 30.0, isActive = true)
    )

    val sampleAddress = AddressEntity(
        id = 1,
        fullName = "Farhana Nadeem",
        phone = "+92 300 9876543",
        street = "House #45, Gulberg III, Main Boulevard",
        city = "Lahore",
        postalCode = "54000",
        isDefault = true
    )

    val sampleWallet = WalletEntity(
        id = 1,
        balance = 250.00,
        rewardPoints = 850,
        referralCode = "FARHANA-3BRO"
    )

    val sampleReviews = listOf(
        ReviewEntity(
            productId = "p1",
            userName = "Kamran Ali",
            userRating = 5.0f,
            comment = "Outstanding watch! The gold finishing is genuine and luxury weight. Packaging from Three Brothers was superb.",
            date = "2026-07-20"
        ),
        ReviewEntity(
            productId = "p1",
            userName = "Amina Tariq",
            userRating = 4.8f,
            comment = "Gifted this to my husband for our anniversary. Mrs. Farhana Nadeem's handwritten thank you card was a lovely touch!",
            date = "2026-07-18"
        ),
        ReviewEntity(
            productId = "p2",
            userName = "Saba Sheikh",
            userRating = 5.0f,
            comment = "The embroidery is even more vibrant in person. Fits perfectly! High quality silk.",
            date = "2026-07-21"
        )
    )
}
