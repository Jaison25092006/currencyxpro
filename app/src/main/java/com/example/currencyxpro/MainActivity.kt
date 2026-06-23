package com.example.currencyxpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.currencyxpro.data.local.SettingsManager
import com.example.currencyxpro.theme.CurrencyXProTheme
import com.example.currencyxpro.ui.navigation.NavGraph
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  @Inject
  lateinit var settingsManager: SettingsManager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    enableEdgeToEdge()
    setContent {
      val themePref by settingsManager.themeState.collectAsState()

      val useDarkTheme = when (themePref) {
        SettingsManager.THEME_LIGHT -> false
        SettingsManager.THEME_DARK -> true
        else -> isSystemInDarkTheme()
      }

      CurrencyXProTheme(darkTheme = useDarkTheme) {
        val navController = rememberNavController()
        Surface(modifier = Modifier.fillMaxSize()) {
          NavGraph(navController = navController)
        }
      }
    }
  }
}
