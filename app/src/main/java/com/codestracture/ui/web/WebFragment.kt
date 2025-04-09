package com.codestracture.ui.web

import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import com.codestracture.R
import com.codestracture.databinding.FragmentWebBinding
import com.codestracture.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebFragment : BaseFragment<FragmentWebBinding, WebViewModel>() {

    override val viewModel: WebViewModel by viewModels()

    override val layoutId: Int = R.layout.fragment_web

    override fun observeEvents() {
    }

    override fun initView() {
        val ws: WebSettings = binding.webView.settings
        ws.allowContentAccess = true
        ws.domStorageEnabled = true
        ws.allowFileAccess = true
        ws.loadsImagesAutomatically = true
        ws.databaseEnabled = true
        ws.cacheMode = WebSettings.LOAD_NO_CACHE
        ws.javaScriptEnabled = true
        ws.databaseEnabled = true
        WebView.setWebContentsDebuggingEnabled(true)

        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(e: ConsoleMessage): Boolean {
                Log.d("MyTag", "onConsoleMessage:${e.message()}")
                return super.onConsoleMessage(e)
            }
        }

        binding.webView.loadUrl("https://lazycatlabs.com/")
    }
}
