package com.example.currencyxpro.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("currencyx_prefs", Context.MODE_PRIVATE)

    private val _themeState = MutableStateFlow(getTheme())
    val themeState: StateFlow<Int> = _themeState

    private val _defaultBaseState = MutableStateFlow(getDefaultBaseCurrency())
    val defaultBaseState: StateFlow<String> = _defaultBaseState

    private val _notificationsState = MutableStateFlow(getNotificationsEnabled())
    val notificationsState: StateFlow<Boolean> = _notificationsState

    companion object {
        private const val KEY_THEME = "key_theme"
        private const val KEY_DEFAULT_BASE = "key_default_base"
        private const val KEY_NOTIFICATIONS = "key_notifications"

        const val THEME_SYSTEM = 0
        const val THEME_LIGHT = 1
        const val THEME_DARK = 2
    }

    fun getTheme(): Int {
        return prefs.getInt(KEY_THEME, THEME_SYSTEM)
    }

    fun setTheme(theme: Int) {
        prefs.edit().putInt(KEY_THEME, theme).apply()
        _themeState.value = theme
    }

    fun getDefaultBaseCurrency(): String {
        return prefs.getString(KEY_DEFAULT_BASE, "USD") ?: "USD"
    }

    fun setDefaultBaseCurrency(currencyCode: String) {
        prefs.edit().putString(KEY_DEFAULT_BASE, currencyCode).apply()
        _defaultBaseState.value = currencyCode
    }

    fun getNotificationsEnabled(): Boolean {
        return prefs.getBoolean(KEY_NOTIFICATIONS, true)
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_NOTIFICATIONS, enabled).apply()
        _notificationsState.value = enabled
    }
}
