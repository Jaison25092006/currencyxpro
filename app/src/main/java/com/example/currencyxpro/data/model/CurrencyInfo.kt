package com.example.currencyxpro.data.model

data class CurrencyInfo(
    val code: String,
    val name: String,
    val symbol: String,
    val flag: String
)

object CurrencyData {
    val supportedCurrencies = listOf(
        CurrencyInfo("USD", "United States Dollar", "$", "🇺🇸"),
        CurrencyInfo("EUR", "Euro", "€", "🇪🇺"),
        CurrencyInfo("GBP", "British Pound", "£", "🇬🇧"),
        CurrencyInfo("INR", "Indian Rupee", "₹", "🇮🇳"),
        CurrencyInfo("AED", "UAE Dirham", "د.إ", "🇦🇪"),
        CurrencyInfo("JPY", "Japanese Yen", "¥", "🇯🇵"),
        CurrencyInfo("CAD", "Canadian Dollar", "$", "🇨🇦"),
        CurrencyInfo("AUD", "Australian Dollar", "$", "🇦🇺"),
        CurrencyInfo("SGD", "Singapore Dollar", "$", "🇸🇬"),
        CurrencyInfo("CNY", "Chinese Yuan", "¥", "🇨🇳")
    )

    fun getCurrencyInfo(code: String): CurrencyInfo {
        return supportedCurrencies.find { it.code.equals(code, ignoreCase = true) }
            ?: CurrencyInfo(code, code, "", "🏳️")
    }
}
