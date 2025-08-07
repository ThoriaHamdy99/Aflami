package com.amsterdam.ui.screens.resetPassword.components

import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView


@Composable
fun ResetPasswordWebView(
    modifier: Modifier = Modifier,
    urlToLoad: String,
    onLoadingStateChanged: (isLoading: Boolean) -> Unit,
    onResetPasswordComplete: () -> Unit,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true

                webViewClient = object : WebViewClient() {

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        onLoadingStateChanged(false)
                    }

                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        super.onReceivedError(view, request, error)
                        onLoadingStateChanged(false)
                    }

                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        val newUrl = request?.url?.toString()
                        val resetPasswordUrl = "https://www.themoviedb.org/reset-password"

                        if (newUrl != null && newUrl != resetPasswordUrl) {
                            onResetPasswordComplete()
                            return true
                        }

                        return false
                    }
                }

                onLoadingStateChanged(false)
                loadUrl(urlToLoad)
            }
        }
    )
}