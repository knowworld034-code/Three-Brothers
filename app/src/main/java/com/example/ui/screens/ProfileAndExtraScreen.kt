package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.WalletEntity
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.Language

@Composable
fun ProfileAndExtraScreen(
    userName: String,
    userEmail: String,
    userPhone: String,
    wallet: WalletEntity?,
    language: Language,
    isDarkMode: Boolean,
    onLanguageClick: () -> Unit,
    onDarkModeToggle: () -> Unit,
    onOpenAiAssistant: () -> Unit,
    onOpenQrPay: () -> Unit
) {
    val context = LocalContext.current

    var selectedInfoDialog by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 80.dp)
    ) {
        // User Profile Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NavyPrimary)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(GoldAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(32.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(userName, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                    Text(userEmail, color = Color.LightGray, fontSize = 11.sp)
                    Text(userPhone, color = GoldAccent, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Wallet & Loyalty Rewards
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = GoldAccent.copy(alpha = 0.15f)),
            border = BorderStroke(1.dp, GoldAccent)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Three Brothers Wallet", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("Balance: $${wallet?.balance ?: 250.00}", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = NavyPrimary)
                    Text("Loyalty Rewards: ${wallet?.rewardPoints ?: 850} Points", fontSize = 11.sp, color = GoldAccent)
                }

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
                ) {
                    Text("Top Up", fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Referral Code Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Your Referral Code", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("FARHANA-3BRO", fontWeight = FontWeight.ExtraBold, color = GoldAccent, fontSize = 14.sp)
                }
                OutlinedButton(onClick = {}) {
                    Icon(Icons.Default.Share, null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Share", fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Settings & App Options
        Text("App Preferences & Support", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))

        ProfileOptionTile(
            icon = Icons.Default.Language,
            title = "Language: ${language.displayName}",
            onClick = onLanguageClick
        )

        ProfileOptionTile(
            icon = Icons.Default.DarkMode,
            title = "Dark / Light Mode Toggle",
            onClick = onDarkModeToggle
        )

        ProfileOptionTile(
            icon = Icons.Default.QrCodeScanner,
            title = "QR Code Payments",
            onClick = onOpenQrPay
        )

        ProfileOptionTile(
            icon = Icons.Default.AutoAwesome,
            title = "AI Shopping Assistant",
            onClick = onOpenAiAssistant
        )

        ProfileOptionTile(
            icon = Icons.Default.SupportAgent,
            title = "24/7 WhatsApp Customer Support",
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/923001234567?text=Hello%20Three%20Brothers%20Support"))
                context.startActivity(intent)
            }
        )

        ProfileOptionTile(
            icon = Icons.Default.Help,
            title = "Frequently Asked Questions (FAQ)",
            onClick = { selectedInfoDialog = "FAQ" }
        )

        ProfileOptionTile(
            icon = Icons.Default.Info,
            title = "About Mrs. Farhana Nadeem & Three Brothers",
            onClick = { selectedInfoDialog = "ABOUT" }
        )

        ProfileOptionTile(
            icon = Icons.Default.Policy,
            title = "Privacy Policy & Terms",
            onClick = { selectedInfoDialog = "TERMS" }
        )
    }

    if (selectedInfoDialog != null) {
        AlertDialog(
            onDismissRequest = { selectedInfoDialog = null },
            title = {
                Text(
                    when (selectedInfoDialog) {
                        "FAQ" -> "Frequently Asked Questions"
                        "ABOUT" -> "About Three Brothers"
                        else -> "Privacy Policy & Terms"
                    }
                )
            },
            text = {
                Text(
                    when (selectedInfoDialog) {
                        "FAQ" -> "Q: How long does delivery take?\nA: Standard delivery takes 1-2 business days across all major cities.\n\nQ: Is Cash on Delivery available?\nA: Yes! COD is available on all items."
                        "ABOUT" -> "Three Brothers is a premier luxury e-commerce destination founded and owned by Mrs. Farhana Nadeem. We specialize in high-end watches, designer couture, fine jewelry, electronics, and authentic fragrances."
                        else -> "All customer data is encrypted using SSL/TLS protocols. Orders placed with Three Brothers are protected by our 100% genuine product guarantee and 7-day hassle-free return policy."
                    },
                    fontSize = 12.sp
                )
            },
            confirmButton = {
                Button(onClick = { selectedInfoDialog = null }) { Text("Close") }
            }
        )
    }
}

@Composable
fun ProfileOptionTile(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = GoldAccent, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
        }
    }
}
