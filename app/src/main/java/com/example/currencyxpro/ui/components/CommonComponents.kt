package com.example.currencyxpro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.currencyxpro.data.model.CurrencyData
import com.example.currencyxpro.data.model.CurrencyInfo
import com.example.currencyxpro.theme.PrimaryDark
import com.example.currencyxpro.theme.PrimaryLight
import com.example.currencyxpro.theme.TertiaryDark
import com.example.currencyxpro.ui.navigation.Screen

@Composable
fun PremiumGradientBackground(
    content: @Composable () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val bgColors = if (isDark) {
        listOf(
            Color(0xFF060913), // Deep Obsidian Navy
            Color(0xFF0F172A), // Deep Navy Slate
            Color(0xFF03050A)  // Darkest Space Black
        )
    } else {
        listOf(
            Color(0xFFEDF2F7), // Lavender Soft White
            Color(0xFFF7FAFC), // Pure Soft Slate
            Color(0xFFE2E8F0)  // Sleek Gray White
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = bgColors))
    ) {
        content()
    }
}

@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(24.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) {
        Color(0xFF0F172A).copy(alpha = 0.55f)
    } else {
        Color(0xFFFFFFFF).copy(alpha = 0.80f)
    }
    val borderColor = if (isDark) {
        Color(0xFF334155).copy(alpha = 0.35f)
    } else {
        Color(0xFFE2E8F0).copy(alpha = 0.65f)
    }

    Card(
        modifier = modifier
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = if (isDark) 0.12f else 0.45f),
                        borderColor
                    )
                ),
                shape = shape
            ),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), content = content)
    }
}

@Composable
fun CurrencySelectorDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onCurrencySelected: (CurrencyInfo) -> Unit,
    currencies: List<CurrencyInfo>
) {
    if (!showDialog) return

    var searchQuery by remember { mutableStateOf("") }
    val filteredCurrencies = currencies.filter {
        it.code.contains(searchQuery, ignoreCase = true) ||
                it.name.contains(searchQuery, ignoreCase = true)
    }

    Dialog(onDismissRequest = onDismiss) {
        GlassmorphicCard(
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Select Currency",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search currency...", color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.secondary) },
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // List
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(filteredCurrencies) { currency ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                onCurrencySelected(currency)
                                onDismiss()
                            }
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Flag Emoji
                        Text(
                            text = currency.flag,
                            fontSize = 32.sp,
                            modifier = Modifier.padding(end = 16.dp)
                        )

                        // Currency Info
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currency.code,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = currency.name,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Symbol
                        Text(
                            text = currency.symbol,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppBottomNavigation(
    navController: NavController,
    items: List<NavigationItem>
) {
    val isDark = isSystemInDarkTheme()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        val containerColor = if (isDark) {
            Color(0xFF0F172A).copy(alpha = 0.85f)
        } else {
            Color(0xFFFFFFFF).copy(alpha = 0.90f)
        }
        val borderColor = if (isDark) {
            Color(0xFF334155).copy(alpha = 0.5f)
        } else {
            Color(0xFFE2E8F0).copy(alpha = 0.8f)
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (isDark) 0.15f else 0.45f),
                            borderColor
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                ),
            shape = RoundedCornerShape(24.dp),
            color = containerColor,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true
                    
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                if (!selected) {
                                    navController.navigate(item.screen.route) {
                                        popUpTo(Screen.Home.route) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                            .padding(vertical = 6.dp, horizontal = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.title,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 10.sp,
                            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

data class NavigationItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val screen: Screen
)

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val brush = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            TertiaryDark
        )
    )
    Box(
        modifier = modifier
            .height(56.dp)
            .background(brush = brush, shape = RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
