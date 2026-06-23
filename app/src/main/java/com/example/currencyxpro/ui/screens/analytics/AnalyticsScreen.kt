package com.example.currencyxpro.ui.screens.analytics

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyxpro.data.model.CurrencyData
import com.example.currencyxpro.data.model.Resource
import com.example.currencyxpro.theme.AccentGreen
import com.example.currencyxpro.theme.AccentRed
import com.example.currencyxpro.ui.components.CurrencySelectorDialog
import com.example.currencyxpro.ui.components.GlassmorphicCard
import com.example.currencyxpro.ui.components.PremiumGradientBackground
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.util.Locale

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val baseCurrency by viewModel.baseCurrency.collectAsState()
    val targetCurrency by viewModel.targetCurrency.collectAsState()
    val timeRangeDays by viewModel.timeRangeDays.collectAsState()
    val historyDataState by viewModel.historyDataState.collectAsState()

    var showBaseDialog by remember { mutableStateOf(false) }
    var showTargetDialog by remember { mutableStateOf(false) }

    val baseInfo = CurrencyData.getCurrencyInfo(baseCurrency)
    val targetInfo = CurrencyData.getCurrencyInfo(targetCurrency)

    val isDark = isSystemInDarkTheme()

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
                        text = "Market Trends",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "Historical charts and volatility stats",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                IconButton(
                    onClick = { viewModel.loadHistoricalRates() },
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
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // Currency selector glass row
                GlassmorphicCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Base Dropdown
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { showBaseDialog = true }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(baseInfo.flag, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(baseCurrency, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }

                        // Swap Button
                        IconButton(
                            onClick = { viewModel.swapCurrencies() },
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    shape = CircleShape
                                )
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Swap",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        // Target Dropdown
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { showTargetDialog = true }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(targetInfo.flag, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(targetCurrency, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Time Range Segment
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TimeRangeTab(
                        label = "7 Days",
                        selected = timeRangeDays == 7,
                        onClick = { viewModel.setTimeRangeDays(7) }
                    )
                    TimeRangeTab(
                        label = "30 Days",
                        selected = timeRangeDays == 30,
                        onClick = { viewModel.setTimeRangeDays(30) }
                    )
                    TimeRangeTab(
                        label = "90 Days",
                        selected = timeRangeDays == 90,
                        onClick = { viewModel.setTimeRangeDays(90) }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Charts Content
                when (historyDataState) {
                    is Resource.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                    is Resource.Error -> {
                        val errorMsg = (historyDataState as Resource.Error).message
                        GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = errorMsg,
                                    color = MaterialTheme.colorScheme.error,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    is Resource.Success -> {
                        val historicalRates = (historyDataState as Resource.Success<List<Pair<String, Double>>>).data
                        
                        if (historicalRates.isNotEmpty()) {
                            val firstRate = historicalRates.first().second
                            val lastRate = historicalRates.last().second
                            val percentChange = ((lastRate - firstRate) / firstRate) * 100
                            val isPositive = percentChange >= 0.0

                            val highVal = historicalRates.maxOf { it.second }
                            val lowVal = historicalRates.minOf { it.second }
                            val avgVal = historicalRates.map { it.second }.average()

                            // Volatility Graph Card
                            GlassmorphicCard(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "RATE SPOTLIGHT",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.primary,
                                            letterSpacing = 1.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "1 $baseCurrency = ${String.format(Locale.US, "%.5f", lastRate)} $targetCurrency",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Black,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .background(
                                                color = (if (isPositive) AccentGreen else AccentRed).copy(alpha = 0.1f),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isPositive) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                                            contentDescription = null,
                                            tint = if (isPositive) AccentGreen else AccentRed,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = String.format(Locale.US, "%+.2f%%", percentChange),
                                            color = if (isPositive) AccentGreen else AccentRed,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                // Line Chart implementation
                                AndroidView(
                                    factory = { ctx ->
                                        LineChart(ctx).apply {
                                            description.isEnabled = false
                                            setTouchEnabled(true)
                                            isDragEnabled = true
                                            isScaleXEnabled = true
                                            isScaleYEnabled = true
                                            setPinchZoom(true)
                                            setDrawGridBackground(false)
                                            
                                            // Configure grid elements
                                            xAxis.isEnabled = false
                                            axisRight.isEnabled = false
                                            legend.isEnabled = false
                                            
                                            axisLeft.apply {
                                                textColor = if (isDark) android.graphics.Color.WHITE else android.graphics.Color.DKGRAY
                                                textSize = 10f
                                                setDrawGridLines(true)
                                                gridColor = if (isDark) android.graphics.Color.parseColor("#1E293B") else android.graphics.Color.parseColor("#E2E8F0")
                                                gridLineWidth = 1f
                                                setDrawAxisLine(false)
                                            }
                                        }
                                    },
                                    update = { chart ->
                                        val entries = historicalRates.mapIndexed { index, pair ->
                                            Entry(index.toFloat(), pair.second.toFloat())
                                        }
                                        
                                        val dataSet = LineDataSet(entries, "Rates").apply {
                                            color = android.graphics.Color.parseColor(if (isDark) "#818CF8" else "#4F46E5")
                                            setDrawCircles(false)
                                            lineWidth = 3.5f
                                            setDrawValues(false)
                                            mode = LineDataSet.Mode.CUBIC_BEZIER
                                            
                                            setDrawFilled(true)
                                            fillColor = android.graphics.Color.parseColor(if (isDark) "#818CF8" else "#4F46E5")
                                            fillAlpha = 25
                                        }
                                        
                                        chart.data = LineData(dataSet)
                                        chart.invalidate()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(220.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // High, Low, Average grid
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                StatCard(
                                    modifier = Modifier.weight(1f),
                                    label = "MARKET HIGH",
                                    value = String.format(Locale.US, "%.4f", highVal),
                                    color = AccentGreen
                                )
                                StatCard(
                                    modifier = Modifier.weight(1f),
                                    label = "MARKET LOW",
                                    value = String.format(Locale.US, "%.4f", lowVal),
                                    color = AccentRed
                                )
                                StatCard(
                                    modifier = Modifier.weight(1f),
                                    label = "RUNNING AVG",
                                    value = String.format(Locale.US, "%.4f", avgVal),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(100.dp)) // Floating nav spacer
            }
        }
    }

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

@Composable
fun TimeRangeTab(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    color: Color
) {
    GlassmorphicCard(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                fontSize = 9.sp,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
        }
    }
}
