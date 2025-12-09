package com.example.bps.utils

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.graphics.toArgb
import com.example.bps.theme.Orange500

fun launchInAppBrowser(
    context: Context,
    url: String
) {
    try {
        val colorParams = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(Orange500.toArgb()) // Set warna di sini
            .build()
        val intent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setUrlBarHidingEnabled(true)
            .setDefaultColorSchemeParams(colorParams) // Warna Header Browser
            .build()

        intent.launchUrl(context, Uri.parse(url))
    } catch (e: Exception) {
        // Fallback ke browser eksternal biasa jika error
        val browserIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }
}