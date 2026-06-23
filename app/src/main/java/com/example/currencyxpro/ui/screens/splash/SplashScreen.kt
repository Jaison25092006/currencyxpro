package com.example.currencyxpro.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyxpro.theme.TertiaryDark

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onInitializationComplete: () -> Unit
) {
    val isReady by viewModel.isReady.collectAsState()
    
    // Scale animation for logo
    val logoScale = remember { Animatable(0f) }
    
    // Pulsing glow animation
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    LaunchedEffect(key1 = true) {
        logoScale.animateTo(
            targetValue = 1.2f,
            animationSpec = tween(durationMillis = 800)
        )
        logoScale.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(durationMillis = 400)
        )
    }
    
    LaunchedEffect(key1 = isReady) {
        if (isReady) {
            onInitializationComplete()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF04060F), // Ultra dark slate navy
                        Color(0xFF0C101F), // Sleek obsidian blue
                        Color(0xFF020409)  // Rich absolute black
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Glowing Ambient Element
        Box(
            modifier = Modifier
                .size(220.dp)
                .scale(glowScale)
                .alpha(glowAlpha)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            TertiaryDark.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Icon(
                imageVector = Icons.Default.CurrencyExchange,
                contentDescription = "CurrencyX Pro Logo",
                tint = TertiaryDark,
                modifier = Modifier
                    .size(110.dp)
                    .scale(logoScale.value)
            )
            
            Spacer(modifier = Modifier.height(28.dp))
            
            Text(
                text = "CurrencyX Pro",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = (-0.5).sp
            )
            
            Spacer(modifier = Modifier.height(6.dp))
            
            Text(
                text = "Real-time Converter & Exchange Rates",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.25.sp
            )
        }
    }
}
