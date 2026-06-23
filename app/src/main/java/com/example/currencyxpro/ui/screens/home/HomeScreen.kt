package com.example.currencyxpro.ui.screens.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.SwapHoriz
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyxpro.data.model.CurrencyData
import com.example.currencyxpro.data.model.Resource
import com.example.currencyxpro.theme.GoldColor
import com.example.currencyxpro.ui.components.CurrencySelectorDialog
import com.example.currencyxpro.ui.components.GlassmorphicCard
import com.example.currencyxpro.ui.components.GradientButton
import com.example.currencyxpro.ui.components.PremiumGradientBackground
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val baseCurrency by viewModel.baseCurrency.collectAsState()
    val targetCurrency by viewModel.targetCurrency.collectAsState()
    val amount by viewModel.amount.collectAsState()
    val ratesState by viewModel.ratesState.collectAsState()
    val isBaseFav by viewModel.isBaseFavorite.collectAsState()

    var showBaseDialog by remember { mutableStateOf(false) }
    var showTargetDialog by remember { mutableStateOf(false) }
    var rotationAngle by remember { mutableStateOf(0f) }
    val animatedRotation by animateFloatAsState(targetValue = rotationAngle)

    var hasConverted by remember { mutableStateOf(false) }
    var resultAmount by remember { mutableStateOf(0.0) }
    var conversionRate by remember { mutableStateOf(0.0) }

    val baseInfo = CurrencyData.getCurrencyInfo(baseCurrency)
    val targetInfo = CurrencyData.getCurrencyInfo(targetCurrency)

    // Reset results if currency base/target changes
    remember(baseCurrency, targetCurrency) {
        hasConverted = false
        0.0
    }

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
                        text = "CurrencyX Pro",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        letterSpacing = (-0.5).sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFF10B981), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Live Market Rates",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                IconButton(
                    onClick = {
                        viewModel.toggleFavoriteBase()
                        val msg = if (isBaseFav) "$baseCurrency removed from favorites" else "$baseCurrency added to favorites"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
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
                        imageVector = if (isBaseFav) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "Favorite Base",
                        tint = if (isBaseFav) GoldColor else MaterialTheme.colorScheme.onSurfaceVariant,
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
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // Welcome Fintech Dashboard Greeting
                GlassmorphicCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Global Conversion Board",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Amount Input Section
                    Text(
                        text = "AMOUNT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.secondary,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { viewModel.updateAmount(it) },
                        placeholder = { Text("0.00", color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)) },
                        textStyle = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide() }
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.1f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.1f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Overlapping Currency Pickers
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // FROM Selector
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "FROM",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .clickable { showBaseDialog = true }
                                        .padding(horizontal = 14.dp, vertical = 14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(baseInfo.flag, fontSize = 24.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            baseCurrency,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Select Base",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            // TO Selector
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "TO",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .clickable { showTargetDialog = true }
                                        .padding(horizontal = 14.dp, vertical = 14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(targetInfo.flag, fontSize = 24.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            targetCurrency,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Select Target",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        // Floating Swap Button centered overlapping
                        Box(
                            modifier = Modifier
                                .padding(top = 18.dp)
                                .size(44.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                )
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.background,
                                    shape = CircleShape
                                )
                                .clickable {
                                    rotationAngle += 180f
                                    viewModel.swapCurrencies()
                                    hasConverted = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Swap Currencies",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(24.dp)
                                    .rotate(animatedRotation)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Convert Button
                    GradientButton(
                        text = "Calculate Rate",
                        onClick = {
                            keyboardController?.hide()
                            when (ratesState) {
                                is Resource.Success -> {
                                    val rates = (ratesState as Resource.Success<Map<String, Double>>).data
                                    val rate = rates[targetCurrency] ?: 1.0
                                    val inputVal = amount.toDoubleOrNull() ?: 0.0

                                    conversionRate = rate
                                    resultAmount = inputVal * rate
                                    hasConverted = true

                                    viewModel.saveConversionToHistory(inputVal, resultAmount, rate)
                                }
                                else -> {
                                    viewModel.loadRates(forceRefresh = true)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Loading State
                if (ratesState is Resource.Loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }

                // Error State
                if (ratesState is Resource.Error) {
                    val errorMsg = (ratesState as Resource.Error).message
                    GlassmorphicCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = errorMsg,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            TextButton(
                                onClick = { viewModel.loadRates(forceRefresh = true) },
                                modifier = Modifier.background(
                                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "Retry", tint = MaterialTheme.colorScheme.error)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Retry Connection", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Results Card
                AnimatedVisibility(
                    visible = hasConverted && ratesState is Resource.Success,
                    enter = fadeIn(animationSpec = tween(500)) + slideInVertically(animationSpec = tween(500)),
                    exit = fadeOut(animationSpec = tween(300))
                ) {
                    GlassmorphicCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "CONVERSION SPOTLIGHT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            val inputVal = amount.toDoubleOrNull() ?: 0.0
                            Text(
                                text = "${String.format(Locale.US, "%,.2f", inputVal)} $baseCurrency",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )

                            Text(
                                text = "=",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )

                            Text(
                                text = "${String.format(Locale.US, "%,.4f", resultAmount)} $targetCurrency",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center,
                                letterSpacing = (-0.5).sp
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            // Exchange rate sub-box
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "1 $baseCurrency = ${String.format(Locale.US, "%.5f", conversionRate)} $targetCurrency",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    val reverseRate = if (conversionRate > 0.0) 1.0 / conversionRate else 0.0
                                    Text(
                                        text = "1 $targetCurrency = ${String.format(Locale.US, "%.5f", reverseRate)} $baseCurrency",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }

                                Text(
                                    text = "Auto-Saved",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier
                                        .background(
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    // Currency Selector Dialogs
    CurrencySelectorDialog(
        showDialog = showBaseDialog,
        onDismiss = { showBaseDialog = false },
        onCurrencySelected = { viewModel.setBaseCurrency(it.code) },
        currencies = viewModel.getSupportedCurrencies()
    )

    CurrencySelectorDialog(
        showDialog = showTargetDialog,
        onDismiss = { showTargetDialog = false },
        onCurrencySelected = { viewModel.setTargetCurrency(it.code) },
        currencies = viewModel.getSupportedCurrencies()
    )
}
