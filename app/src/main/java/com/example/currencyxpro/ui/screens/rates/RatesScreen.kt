package com.example.currencyxpro.ui.screens.rates

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyxpro.data.model.CurrencyData
import com.example.currencyxpro.data.model.Resource
import com.example.currencyxpro.ui.components.CurrencySelectorDialog
import com.example.currencyxpro.ui.components.GlassmorphicCard
import com.example.currencyxpro.ui.components.PremiumGradientBackground
import java.util.Locale

@Composable
fun RatesScreen(
    viewModel: RatesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val baseCurrency by viewModel.baseCurrency.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val ratesState by viewModel.ratesState.collectAsState()

    var showBaseDialog by remember { mutableStateOf(false) }
    var expandedCurrencyCode by remember { mutableStateOf<String?>(null) }
    val baseInfo = CurrencyData.getCurrencyInfo(baseCurrency)

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Live Exchange Board",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "Tap any card to reveal details & math",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                IconButton(
                    onClick = {
                        viewModel.loadRates(forceRefresh = true)
                        Toast.makeText(context, "Rates updated!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                            shape = CircleShape
                        )
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f), CircleShape)
                        .size(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        PremiumGradientBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
            ) {
                // Base selector row
                GlassmorphicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showBaseDialog = true }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(baseInfo.flag, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "SELECTED BASE",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "${baseInfo.code} - ${baseInfo.name}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select Base",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    placeholder = { Text("Search currency by code or name...", color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.secondary) },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.1f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.1f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Rates Content
                when (ratesState) {
                    is Resource.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                    is Resource.Error -> {
                        val errorMsg = (ratesState as Resource.Error).message
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = errorMsg,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.loadRates(forceRefresh = true) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Retry Connection")
                            }
                        }
                    }
                    is Resource.Success -> {
                        val rates = (ratesState as Resource.Success<Map<String, Double>>).data
                        
                        val rateItems = CurrencyData.supportedCurrencies
                            .filter { it.code != baseCurrency }
                            .filter {
                                it.code.contains(searchQuery, ignoreCase = true) ||
                                        it.name.contains(searchQuery, ignoreCase = true)
                            }

                        if (rateItems.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No rates found for \"$searchQuery\"",
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontSize = 14.sp
                                )
                            }
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                items(rateItems) { item ->
                                    val rateVal = rates[item.code] ?: 0.0
                                    val isExpanded = expandedCurrencyCode == item.code

                                    GlassmorphicCard(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .animateContentSize()
                                            .clickable {
                                                expandedCurrencyCode = if (isExpanded) null else item.code
                                            }
                                    ) {
                                        // Header Row
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                modifier = Modifier.weight(1f),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(item.flag, fontSize = 28.sp)
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Column {
                                                    Text(
                                                        text = item.code,
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                    Text(
                                                        text = item.name,
                                                        fontSize = 11.sp,
                                                        color = MaterialTheme.colorScheme.secondary
                                                    )
                                                }
                                            }

                                            if (rateVal > 0.0) {
                                                Text(
                                                    text = String.format(Locale.US, "%.5f", rateVal),
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = MaterialTheme.colorScheme.primary,
                                                    textAlign = TextAlign.End
                                                )
                                            } else {
                                                Text(
                                                    text = "--",
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.secondary
                                                )
                                            }
                                        }

                                        // Expanded detail
                                        if (isExpanded && rateVal > 0.0) {
                                            Spacer(modifier = Modifier.height(14.dp))
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(1.dp)
                                                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                                            )
                                            Spacer(modifier = Modifier.height(12.dp))

                                            Text(
                                                text = "EXCHANGE MATH & DETAILS",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = MaterialTheme.colorScheme.primary,
                                                letterSpacing = 1.sp
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Column {
                                                    Text(
                                                        text = "Base to Target:",
                                                        fontSize = 12.sp,
                                                        color = MaterialTheme.colorScheme.secondary
                                                    )
                                                    Text(
                                                        text = "1 $baseCurrency = ${String.format(Locale.US, "%.5f", rateVal)} ${item.code}",
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                }
                                                Column(horizontalAlignment = Alignment.End) {
                                                    Text(
                                                        text = "Reciprocal Rate:",
                                                        fontSize = 12.sp,
                                                        color = MaterialTheme.colorScheme.secondary
                                                    )
                                                    val reciprocal = 1.0 / rateVal
                                                    Text(
                                                        text = "1 ${item.code} = ${String.format(Locale.US, "%.5f", reciprocal)} $baseCurrency",
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(14.dp))

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End
                                            ) {
                                                TextButton(
                                                    onClick = {
                                                        viewModel.setBaseCurrency(item.code)
                                                        expandedCurrencyCode = null
                                                        Toast.makeText(context, "Base set to ${item.code}", Toast.LENGTH_SHORT).show()
                                                    },
                                                    modifier = Modifier.background(
                                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                                        shape = RoundedCornerShape(12.dp)
                                                    )
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.SwapHoriz,
                                                        contentDescription = "Set Base",
                                                        modifier = Modifier.size(16.dp),
                                                        tint = MaterialTheme.colorScheme.primary
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Make Base", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                        }
                                    }
                                }
                                item {
                                    Spacer(modifier = Modifier.height(90.dp)) // Avoid floating navigation overlapping content at the absolute bottom
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    CurrencySelectorDialog(
        showDialog = showBaseDialog,
        onDismiss = { showBaseDialog = false },
        onCurrencySelected = { viewModel.setBaseCurrency(it.code) },
        currencies = viewModel.getSupportedCurrencies()
    )
}
