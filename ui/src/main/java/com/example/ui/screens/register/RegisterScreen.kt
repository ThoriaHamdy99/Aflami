package com.example.ui.screens.register

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.designsystem.theme.AppTheme

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun RegisterScreen() {
    val context = LocalContext.current
    val url = "https://www.themoviedb.org/signup"
    AndroidView(
        modifier =
            Modifier
                .fillMaxSize()
                .background(AppTheme.color.surface)
                .statusBarsPadding()
                .navigationBarsPadding(),
        factory = {
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadUrl(url)
            }
        },
    )

}
