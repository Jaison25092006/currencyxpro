package com.example.currencyxpro.ui.screens.history

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyxpro.data.model.CurrencyData
import com.example.currencyxpro.ui.components.GlassmorphicCard
import com.example.currencyxpro.ui.components.PremiumGradientBackground
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val historyList by viewModel.historyState.collectAsState()
    val dateFormatter = rememberDateFormatter()

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
                        text = "History Logs",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "Your local logs of completed computations",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                if (historyList.isNotEmpty()) {
                    TextButton(
                        onClick = {
                            viewModel.clearAllHistory()
                            Toast.makeText(context, "History cleared!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Clear All",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Clear Logs", fontSize = 12.sp, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        PremiumGradientBackground {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
            ) {
                if (historyList.isEmpty()) {
                    GlassmorphicCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "No History",
                                tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No History Found",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Run calculations on the converter board and they will be listed here automatically.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(historyList) { historyItem ->
                            val fromInfo = CurrencyData.getCurrencyInfo(historyItem.fromCurrency)
                            val toInfo = CurrencyData.getCurrencyInfo(historyItem.toCurrency)
                            val formattedDate = dateFormatter.format(Date(historyItem.timestamp))

                            GlassmorphicCard(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("${fromInfo.flag} ${fromInfo.code}", fontWeight = FontWeight.Black, fontSize = 14.sp)
                                            Text(" → ", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                            Text("${toInfo.flag} ${toInfo.code}", fontWeight = FontWeight.Black, fontSize = 14.sp)
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = "${String.format(Locale.US, "%.2f", historyItem.fromAmount)} ${historyItem.fromCurrency} = " +
                                                    "${String.format(Locale.US, "%.4f", historyItem.toAmount)} ${historyItem.toCurrency}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Black,
                                            color = MaterialTheme.colorScheme.primary
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = "Rate: 1 ${historyItem.fromCurrency} = ${String.format(Locale.US, "%.5f", historyItem.rate)} ${historyItem.toCurrency} | $formattedDate",
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            viewModel.deleteHistory(historyItem)
                                            Toast.makeText(context, "History record deleted", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier
                                            .background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f), CircleShape)
                                            .size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete Item",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(18.dp)
                                        )
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

@Composable
fun rememberDateFormatter(): SimpleDateFormat {
    return remember {
        SimpleDateFormat("MMM dd, yyyy, hh:mm a", Locale.getDefault())
    }
}
