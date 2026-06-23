package com.example.currencyxpro.ui.screens.favorites

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyxpro.data.local.entity.FavoritePairEntity
import com.example.currencyxpro.data.model.CurrencyData
import com.example.currencyxpro.data.model.Resource
import com.example.currencyxpro.theme.GoldColor
import com.example.currencyxpro.ui.components.CurrencySelectorDialog
import com.example.currencyxpro.ui.components.GlassmorphicCard
import com.example.currencyxpro.ui.components.GradientButton
import com.example.currencyxpro.ui.components.PremiumGradientBackground
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val baseCurrency by viewModel.baseCurrency.collectAsState()
    val favorites by viewModel.favoritesState.collectAsState()
    val favoritePairs by viewModel.favoritePairsState.collectAsState()
    val ratesState by viewModel.ratesState.collectAsState()

    var activeTab by remember { mutableIntStateOf(0) } // 0 = Currencies, 1 = Pairs
    var showBaseDialog by remember { mutableStateOf(false) }

    // State for Quick Convert Dialog
    var showQuickConvertDialog by remember { mutableStateOf(false) }
    var quickConvertPair by remember { mutableStateOf<FavoritePairEntity?>(null) }
    var quickConvertAmount by remember { mutableStateOf("1") }

    // State for Add Pair Dialog
    var showAddPairDialog by remember { mutableStateOf(false) }
    var pairBaseCurrency by remember { mutableStateOf("USD") }
    var pairTargetCurrency by remember { mutableStateOf("EUR") }
    var selectingPairBase by remember { mutableStateOf(false) }
    var selectingPairTarget by remember { mutableStateOf(false) }

    val baseInfo = CurrencyData.getCurrencyInfo(baseCurrency)

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Favorites Space",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "Track your favorite assets & custom pairs",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    if (activeTab == 1) {
                        IconButton(
                            onClick = { showAddPairDialog = true },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Pair",
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Custom Tab Row
                TabRow(
                    selectedTabIndex = activeTab,
                    containerColor = Color.Transparent,
                    divider = {},
                    indicator = { tabPositions ->
                        TabRowDefaults.PrimaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[activeTab]),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                ) {
                    Tab(
                        selected = activeTab == 0,
                        onClick = { activeTab = 0 },
                        text = { Text("Currencies", fontWeight = FontWeight.Bold, fontSize = 14.sp) }
                    )
                    Tab(
                        selected = activeTab == 1,
                        onClick = { activeTab = 1 },
                        text = { Text("Asset Pairs", fontWeight = FontWeight.Bold, fontSize = 14.sp) }
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
                if (activeTab == 0) {
                    // CURRENCIES TAB
                    Spacer(modifier = Modifier.height(6.dp))
                    // Base selector card
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
                                          text = "COMPARE BASE",
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

                    Spacer(modifier = Modifier.height(16.dp))

                    if (favorites.isEmpty()) {
                        GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "No Favorites",
                                    tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                                    modifier = Modifier.size(64.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "No Favorites Yet",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Tap quick-add buttons below to save currencies for rapid rate monitoring.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        Box(modifier = Modifier.weight(1f)) {
                            when (ratesState) {
                                is Resource.Loading -> {
                                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                                is Resource.Error -> {
                                    Text(
                                        text = "Rates offline: ${(ratesState as Resource.Error).message}",
                                        color = MaterialTheme.colorScheme.error,
                                        fontSize = 14.sp
                                    )
                                }
                                is Resource.Success -> {
                                    val rates = (ratesState as Resource.Success<Map<String, Double>>).data
                                    LazyColumn(
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        items(favorites) { favorite ->
                                            val favInfo = CurrencyData.getCurrencyInfo(favorite.code)
                                            val rateVal = rates[favorite.code]

                                            GlassmorphicCard(
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Row(
                                                        modifier = Modifier.weight(1f),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(favInfo.flag, fontSize = 28.sp)
                                                        Spacer(modifier = Modifier.width(12.dp))
                                                        Column {
                                                            Text(
                                                                text = favInfo.code,
                                                                fontSize = 16.sp,
                                                                fontWeight = FontWeight.ExtraBold
                                                            )
                                                            Text(
                                                                text = favInfo.name,
                                                                fontSize = 11.sp,
                                                                color = MaterialTheme.colorScheme.secondary
                                                            )
                                                        }
                                                    }

                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.End
                                                    ) {
                                                        if (rateVal != null) {
                                                            Text(
                                                                text = String.format(Locale.US, "%.5f", rateVal),
                                                                fontSize = 16.sp,
                                                                fontWeight = FontWeight.Black,
                                                                color = MaterialTheme.colorScheme.primary
                                                            )
                                                        } else {
                                                            Text(text = "--", fontSize = 16.sp)
                                                        }

                                                        Spacer(modifier = Modifier.width(12.dp))

                                                        IconButton(
                                                            onClick = { viewModel.removeFavorite(favorite.code) },
                                                            modifier = Modifier
                                                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), CircleShape)
                                                                .size(36.dp)
                                                        ) {
                                                            Icon(Icons.Default.Star, contentDescription = null, tint = GoldColor, modifier = Modifier.size(20.dp))
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        item {
                                            Spacer(modifier = Modifier.height(90.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    // Quick add list
                    Text("QUICK RECOMMENDATIONS", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    val remainingCurrencies = viewModel.getSupportedCurrencies()
                        .filter { it.code != baseCurrency }
                        .filter { info -> favorites.none { it.code == info.code } }

                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 90.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        remainingCurrencies.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.4f))
                                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                                    .clickable { viewModel.addFavorite(item.code) }
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(item.flag)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(item.code, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }

                } else {
                    // ASSET PAIRS TAB
                    if (favoritePairs.isEmpty()) {
                        GlassmorphicCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Calculate,
                                    contentDescription = "No Pairs",
                                    tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                                    modifier = Modifier.size(64.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "No Asset Pairs",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Tap the plus button (+) in the top bar to design customizable asset converter links.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        Box(modifier = Modifier.weight(1f)) {
                            when (ratesState) {
                                is Resource.Loading -> {
                                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                                is Resource.Error -> {
                                    Text(text = "Rates offline.", color = MaterialTheme.colorScheme.error)
                                }
                                is Resource.Success -> {
                                    val rates = (ratesState as Resource.Success<Map<String, Double>>).data
                                    LazyColumn(
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        items(favoritePairs) { pair ->
                                            val baseInf = CurrencyData.getCurrencyInfo(pair.baseCurrency)
                                            val targetInf = CurrencyData.getCurrencyInfo(pair.targetCurrency)

                                            GlassmorphicCard(
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Column {
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Text("${baseInf.flag} ${pair.baseCurrency}", fontWeight = FontWeight.Black, fontSize = 16.sp)
                                                            Text(" → ", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                                            Text("${targetInf.flag} ${pair.targetCurrency}", fontWeight = FontWeight.Black, fontSize = 16.sp)
                                                        }
                                                        Spacer(modifier = Modifier.height(4.dp))
                                                        Text(
                                                            text = "${baseInf.name} to ${targetInf.name}",
                                                            fontSize = 11.sp,
                                                            color = MaterialTheme.colorScheme.secondary
                                                        )
                                                    }

                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        // Quick Convert Button
                                                        IconButton(
                                                            onClick = {
                                                                quickConvertPair = pair
                                                                quickConvertAmount = "1"
                                                                showQuickConvertDialog = true
                                                            },
                                                            modifier = Modifier
                                                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                                                                .size(36.dp)
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.Calculate,
                                                                contentDescription = "Quick Convert",
                                                                tint = Color.White,
                                                                modifier = Modifier.size(18.dp)
                                                            )
                                                        }

                                                        Spacer(modifier = Modifier.width(10.dp))

                                                        IconButton(
                                                            onClick = { viewModel.removeFavoritePair(pair.baseCurrency, pair.targetCurrency) },
                                                            modifier = Modifier
                                                                .background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f), CircleShape)
                                                                .size(36.dp)
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.Delete,
                                                                contentDescription = "Delete",
                                                                tint = MaterialTheme.colorScheme.error,
                                                                modifier = Modifier.size(18.dp)
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        item {
                                            Spacer(modifier = Modifier.height(90.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Quick Convert Dialog Redesign
    if (showQuickConvertDialog && quickConvertPair != null) {
        val pair = quickConvertPair!!
        val baseInf = CurrencyData.getCurrencyInfo(pair.baseCurrency)
        val targetInf = CurrencyData.getCurrencyInfo(pair.targetCurrency)

        Dialog(onDismissRequest = { showQuickConvertDialog = false }) {
            GlassmorphicCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Instant Calculator", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                    IconButton(onClick = { showQuickConvertDialog = false }) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "${baseInf.flag} ${pair.baseCurrency} to ${targetInf.flag} ${pair.targetCurrency}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = quickConvertAmount,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            quickConvertAmount = it
                        }
                    },
                    placeholder = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                val rates = (ratesState as? Resource.Success)?.data
                val baseRates = rates ?: emptyMap()
                
                val baseToUSD = if (pair.baseCurrency == "USD") 1.0 else (baseRates[pair.baseCurrency] ?: 1.0)
                val targetToUSD = if (pair.targetCurrency == "USD") 1.0 else (baseRates[pair.targetCurrency] ?: 1.0)
                val derivedRate = if (pair.baseCurrency == "USD") targetToUSD else (targetToUSD / baseToUSD)

                val amountVal = quickConvertAmount.toDoubleOrNull() ?: 0.0
                val result = amountVal * derivedRate

                Text(
                    text = "RESULT",
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "${String.format(Locale.US, "%,.2f", amountVal)} ${pair.baseCurrency} = ",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "${String.format(Locale.US, "%,.4f", result)} ${pair.targetCurrency}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Ref: 1 ${pair.baseCurrency} = ${String.format(Locale.US, "%.5f", derivedRate)} ${pair.targetCurrency}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }

    // Add Pair Dialog Redesign
    if (showAddPairDialog) {
        Dialog(onDismissRequest = { showAddPairDialog = false }) {
            GlassmorphicCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Link Currency Pair", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                    IconButton(onClick = { showAddPairDialog = false }) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Base Selector
                Text("SOURCE ASSET", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.secondary, letterSpacing = 0.5.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .clickable { selectingPairBase = true }
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val baseInf = CurrencyData.getCurrencyInfo(pairBaseCurrency)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(baseInf.flag, fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(pairBaseCurrency, fontWeight = FontWeight.Bold)
                    }
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Target Selector
                Text("TARGET ASSET", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.secondary, letterSpacing = 0.5.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .clickable { selectingPairTarget = true }
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val targetInf = CurrencyData.getCurrencyInfo(pairTargetCurrency)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(targetInf.flag, fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(pairTargetCurrency, fontWeight = FontWeight.Bold)
                    }
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }

                Spacer(modifier = Modifier.height(24.dp))

                GradientButton(
                    text = "Save Custom Pair",
                    onClick = {
                        if (pairBaseCurrency == pairTargetCurrency) {
                            Toast.makeText(context, "Base and Target currencies cannot be the same!", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.addFavoritePair(pairBaseCurrency, pairTargetCurrency)
                            showAddPairDialog = false
                            Toast.makeText(context, "Favorite pair saved!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Selection sheets
        CurrencySelectorDialog(
            showDialog = selectingPairBase,
            onDismiss = { selectingPairBase = false },
            onCurrencySelected = { pairBaseCurrency = it.code },
            currencies = viewModel.getSupportedCurrencies()
        )

        CurrencySelectorDialog(
            showDialog = selectingPairTarget,
            onDismiss = { selectingPairTarget = false },
            onCurrencySelected = { pairTargetCurrency = it.code },
            currencies = viewModel.getSupportedCurrencies()
        )
    }

    CurrencySelectorDialog(
        showDialog = showBaseDialog,
        onDismiss = { showBaseDialog = false },
        onCurrencySelected = { viewModel.setBaseCurrency(it.code) },
        currencies = viewModel.getSupportedCurrencies()
    )
}
