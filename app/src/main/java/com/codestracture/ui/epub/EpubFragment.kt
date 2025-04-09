package com.codestracture.ui.epub

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.JavascriptInterface
import android.webkit.MimeTypeMap
import android.webkit.WebChromeClient
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.codestracture.R
import com.codestracture.databinding.FragmentEpubBinding
import com.codestracture.ui.base.BaseFragment
import com.codestracture.utils.EpubUtils
import dagger.hilt.android.AndroidEntryPoint
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStreamWriter

@AndroidEntryPoint
class EpubFragment : BaseFragment<FragmentEpubBinding, EpubViewModel>() {

    override val layoutId: Int = R.layout.fragment_epub

    override val viewModel: EpubViewModel by viewModels()

    var bookRootPath: String = ""
    var hWnd = Handler(Looper.getMainLooper())
    var totalPagesInt: Int = 0
    var currentPageNumberInt: Int = 1
    val mergeText = StringBuilder()

    override fun observeEvents() {
        binding.btnPrevious.setOnClickListener {
            binding.folioWebView.loadUrl("javascript:goToAnchor('Untitled-1-4')")
        }

        binding.btnDirection.setOnClickListener {
            if (binding.folioWebView.getDirection() == "HORIZONTAL") {
                binding.folioWebView.setDirection("VERTICAL")
                binding.btnDirection.text = "VERTICAL"
            } else {
                binding.folioWebView.setDirection("HORIZONTAL")
                binding.btnDirection.text = "HORIZONTAL"
            }
            binding.folioWebView.reload()
        }

        binding.btnNext.setOnClickListener {
            binding.folioWebView.loadUrl("javascript:goToAnchor('Untitled-1-5')")
        }

        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            var seekTo = 0

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                seekTo = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                binding.folioWebView.loadUrl("javascript:redirectToPage($seekTo)")
            }
        })

        // Test().checkDirectory(requireContext(),"book/theSelection_fix", "OEBPS")
    }

    override fun initView() {
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        bookRootPath = File(EpubUtils.cacheBookPath, "IndieBook_13455").absolutePath

        val copyFile = File(context?.filesDir, "IndieBook_13455.epub")
        context?.assets?.open("IndieBook_13455.epub").use { input ->
            copyFile.outputStream().use { output ->
                input?.copyTo(output, 1024)
            }
        }

        if (copyFile.exists()) {
            val singlePage = File(bookRootPath, EpubUtils.SINGLE_PAGE)
            if (singlePage.exists()) {
                Log.d("MyTag", "extractFlat:$bookRootPath")
                initBook()
            } else {
                Log.d("MyTag", "extractBook")
                extractBook(copyFile, bookRootPath)
            }
        }
    }

    private fun initBook() {
        val ws: WebSettings = binding.folioWebView.settings
        ws.allowContentAccess = true
        ws.domStorageEnabled = true
        ws.allowFileAccess = true
        ws.loadsImagesAutomatically = true
        ws.databaseEnabled = true
        ws.cacheMode = WebSettings.LOAD_NO_CACHE
        ws.javaScriptEnabled = true
        ws.databaseEnabled = true
        WebView.setWebContentsDebuggingEnabled(true)

        binding.folioWebView.addJavascriptInterface(binding.folioWebView, "FolioWebView")
        binding.folioWebView.addJavascriptInterface(this, "EpubFragment")

        binding.folioWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                applyThemeAndSettings()

                if (binding.folioWebView.getDirection() == "HORIZONTAL") {
                    binding.folioWebView.loadUrl("javascript:initHorizontalDirection()")
                } else {
                    binding.folioWebView.loadUrl("javascript:loadVerticalData()")
                }

                // binding.folioWebView.loadUrl("javascript:searchText('cass')")
                // binding.folioWebView.loadUrl("javascript:findOccurrencesInDivs('cass')")
                // binding.folioWebView.loadUrl("javascript:scrollToPosition('cass', 1)")
            }
        }
        binding.folioWebView.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                Log.d("MyTag", "onProgressChanged::$newProgress")
            }

            override fun onConsoleMessage(e: ConsoleMessage): Boolean {
                Log.d("MyTag", "onConsoleMessage:${e.message()}")
                return super.onConsoleMessage(e)
            }
        }

        binding.folioWebView.init()
        val directory1 = File(context?.cacheDir, "book")
        Log.d("MyTag", "directory1:: ${directory1.path}")
        val server: LocalServer?
        try {
            server = LocalServer(8080, directory1)
            server.start()
        } catch (e: Exception) {
            Log.d("MyTag", "Exception::${e.message}")
        }

//        val fileHtml = resources.assets.open("UNP429-2003080.html").bufferedReader().use{
//            it.readText()
//        }
//        val htmlContent = getHtmlContent(requireContext(), fileHtml)
//        binding.folioWebView.loadDataWithBaseURL(
//            "",
//            htmlContent,
//            "text/html",
//            "UTF-8", null
//        )

        // binding.folioWebView.loadUrl("file:///android_asset/UNP429-2003080.html")

        val directory = File("$bookRootPath/OEBPS/")
        if (directory.isDirectory) {
            val chapterList = directory.listFiles()
                ?.filter { file -> file?.absolutePath?.contains("toc") == false && !file.isDirectory && file.extension == "xhtml" }
            chapterList?.forEach { file ->
                val response = processBookResource(file)
                var responseText = response?.data!!.bufferedReader().use { it.readText() }
                responseText = extractBody(responseText)
                val identifyDiv = applyIdToFirstDiv(responseText, file.nameWithoutExtension)
                mergeText.append(identifyDiv)
            }
        }

        Log.d("MyTag", "bookRootPath:$bookRootPath")

        var htmlContent = getHtmlContent(requireContext(), mergeText.toString())
        // htmlContent =
        // replaceParentDirs(htmlContent, "http://localhost:8080/$bookRootPath/OEBPS/image/")

        binding.folioWebView.loadDataWithBaseURL(
            "http://127.0.0.1:8080/IndieBook_12606/OEBPS/", // "file://$bookRootPath/OEBPS/",
            htmlContent,
            "text/html",
            "UTF-8",
            null
        )
    }

    private fun mergeResponse(href: String, html: String?) {
        if (html != null) {
            // splitHeadLinkCss(html)
            val responseText = extractBody(html)
            val identifyDiv = applyIdToFirstDiv(responseText, href)
            mergeText.append(identifyDiv)
        }
    }

    fun replaceParentDirs(htmlContent: String, replacementPath: String): String {
        // Define a regex pattern to match '../' occurrences
        val pattern = Regex("image/")

        // Replace all occurrences of '../' with the replacement path
        return htmlContent.replace(pattern, replacementPath)
    }

    private fun applyThemeAndSettings() {
        val textAlign = "justify"
        val lineHeight = "1.2"
        val letterSpacing = "0.00em"
        val pageMargins = "0%"
        val wordSpacing = "0.05em"
        val fontFamily = "assistant"
        val fontColor = "#000000"
        val backgroundColor = "#FFFFFF"

        if (fontFamily.isNotEmpty()) {
            injectCss(
                binding.folioWebView,
                "* { font-family: $fontFamily !important; }"
            )
        }
        if (textAlign.isNotEmpty()) {
            injectCss(
                binding.folioWebView,
                "* { text-align: $textAlign !important; }"
            )
        }
        if (lineHeight.isNotEmpty()) {
            injectCss(
                binding.folioWebView,
                "* { line-height: $lineHeight !important; }"
            )
        }
        if (wordSpacing.isNotEmpty()) {
            injectCss(
                binding.folioWebView,
                "* { word-spacing: $wordSpacing !important; }"
            )
        }
        if (letterSpacing.isNotEmpty()) {
            injectCss(
                binding.folioWebView,
                "* { letter-spacing: $letterSpacing !important; }"
            )
        }
        if (pageMargins.isNotEmpty()) {
            injectCss(
                binding.folioWebView,
                "* { margin: $pageMargins !important; }"
            )
        }

        injectCss(binding.folioWebView, "* { color: $fontColor !important; }")
        injectCss(
            binding.folioWebView,
            "html { background-color: $backgroundColor !important; }"
        )
        injectCss(
            binding.folioWebView,
            "body { background-color: $backgroundColor !important; }"
        )
    }

    private fun writeDataToFile(file: File, data: String, writeComplete: () -> Unit) {
        try {
            val fileOutputStream = FileOutputStream(file)
            writeDataToFile(fileOutputStream, data)
            fileOutputStream.close()
            writeComplete()
        } catch (ex: FileNotFoundException) {
            Log.e("LOG_TAG", ex.message, ex)
        } catch (ex: IOException) {
            Log.e("LOG_TAG", ex.message, ex)
        }
    }

    private fun writeDataToFile(fileOutputStream: FileOutputStream, data: String) {
        try {
            val outputStreamWriter = OutputStreamWriter(fileOutputStream)
            val bufferedWriter = BufferedWriter(outputStreamWriter)
            bufferedWriter.write(data)
            bufferedWriter.flush()
            bufferedWriter.close()
            outputStreamWriter.close()
        } catch (ex: IOException) {
            Log.e("LOG_TAG", ex.message, ex)
        }
    }

    private fun applyIdToFirstDiv(html: String, id: String): String {
        var modifiedHtml = ""
        // Define regex patterns
        val openingDivPattern = Regex("<div[^>]*>")
        val closingDivPattern = Regex("</div>")

        // Find the match for the opening <div> tag
        val openingDivMatch = openingDivPattern.find(html)
        if (openingDivMatch != null) {
            val closingDivMatch = closingDivPattern.find(html, openingDivMatch.range.last)
            if (closingDivMatch != null) {
                modifiedHtml = html.replaceRange(openingDivMatch.range, "<div id=\"$id\">")
            } else {
                modifiedHtml = "<div id='$id'>$html</div>"
            }
        } else {
            modifiedHtml = "<div id='$id'>$html</div>"
        }

        return modifiedHtml
    }

    fun extractBody(html: String): String {
        val newHtml = html.substringAfter("<body>").substringBefore("</body>")
        return newHtml.substringAfter("<body dir=\"rtl\">").substringBefore("</body>")
    }

    fun extractBook(be: File, target: String?) {
        EpubUtils.ExtractBook(
            requireContext(), be, target,
            EpubUtils.BookLoadCallback { b ->
                Log.d("MyTag", "ExtractBook::$b")
                if (b) {
                    initBook()
                }
            }
        )
    }

    @Throws(FileNotFoundException::class)
    fun processBookResource(path: File): WebResourceResponse? {
        val mmp = MimeTypeMap.getSingleton()
        val `is`: InputStream = FileInputStream(path)
        val type =
            mmp.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(path.absolutePath))
        return WebResourceResponse(type, null, `is`)
    }

    private fun getHtmlContent(context: Context, htmlContent: String): String {
        var cssPath = String.format(
            context.getString(R.string.css_tag),
            "file:///android_asset/css/Style.css"
        ) + "\n"

        cssPath = cssPath + String.format(
            context.getString(R.string.css_tag),
            "file:///$bookRootPath/OEBPS/Styles/idGeneratedStyles.css"
        ) + "\n"

        var jsPath = String.format(
            context.getString(R.string.script_tag),
            "file:///android_asset/js/JavaScriptFunction.js"
        ) + "\n"

        jsPath = jsPath + String.format(
            context.getString(R.string.script_tag),
            "file:///android_asset/js/jsface.min.js"
        ) + "\n"

        jsPath = jsPath + String.format(
            context.getString(R.string.script_tag),
            "file:///android_asset/js/jquery-3.4.1.min.js"
        ) + "\n"

        jsPath = jsPath + String.format(
            context.getString(R.string.script_tag),
            "file:///android_asset/js/rangy-core.js"
        ) + "\n"

        jsPath = jsPath + String.format(
            context.getString(R.string.script_tag),
            "file:///android_asset/js/rangy-highlighter.js"
        ) + "\n"

        jsPath = jsPath + String.format(
            context.getString(R.string.script_tag),
            "file:///android_asset/js/rangy-classapplier.js"
        ) + "\n"

        jsPath = jsPath + String.format(
            context.getString(R.string.script_tag),
            "file:///android_asset/js/rangy-serializer.js"
        ) + "\n"

        jsPath = jsPath + String.format(
            context.getString(R.string.script_tag),
            "file:///android_asset/js/Bridge.js"
        ) + "\n"

        jsPath = jsPath + String.format(
            context.getString(R.string.script_tag),
            "file:///android_asset/js/rangefix.js"
        ) + "\n"

        jsPath = jsPath + String.format(
            context.getString(R.string.script_tag),
            "file:///android_asset/js/readium-cfi.umd.js"
        ) + "\n"

        jsPath = jsPath + String.format(
            context.getString(R.string.script_tag),
            "file:///android_asset/js/Bridge-after.js"
        ) + "\n"

        val classes = "textSize20"

        val meta = "<meta name=\"viewport\" content=\"height=device-height, user-scalable=no\" />"
        val toInject =
            "<html><head>\n$meta\n$cssPath\n</head><body dir=\"rtl\" id=\"content\">$htmlContent</body>\n$jsPath\n</html>"
        val htmlContentNew = toInject.replace(
            "<html",
            "<html class=\"$classes\" onclick=\"onClickHtml()\""
        )
        return htmlContentNew
    }

    @JavascriptInterface
    fun setHorizontalPageCount(horizontalPageCount: Int) {
        Log.d("MyTag", "setHorizontalPageCount:$horizontalPageCount")
        hWnd.post {
            binding.folioWebView.loadUrl("javascript:loadHorizontalData()")
        }
    }

    @JavascriptInterface
    fun setTotalPages(totalPages: Int) {
        hWnd.post {
            binding.folioWebView.setTotalPages(totalPages)
            this.totalPagesInt = totalPages
            binding.seekBar.max = totalPages
            binding.outOfText.text = "$totalPagesInt / $currentPageNumberInt"
        }
    }

    @JavascriptInterface
    fun setCurrentPageNumber(currentPageNumber: Int) {
        hWnd.post {
            this.currentPageNumberInt = currentPageNumber + 1
            binding.seekBar.progress = currentPageNumber + 1
            binding.outOfText.text = "$totalPagesInt / $currentPageNumberInt"
        }
    }

    private fun injectCss(webView: WebView, vararg cssRules: String) {
        val CREATE_CUSTOM_SHEET =
            ("var customSheet = (function() {" + "var style = document.createElement(\"style\");" + "style.appendChild(document.createTextNode(\"\"));" + "document.head.appendChild(style);" + "return style.sheet;" + "})();" + "}" + "if (typeof(document.head) != 'undefined' && typeof(customSheet) == 'undefined') {")
        val jsUrl = StringBuilder("javascript:")
        jsUrl.append(CREATE_CUSTOM_SHEET)
            .append("if (typeof(customSheet) != 'undefined') {")
        for ((cnt, cssRule) in cssRules.withIndex()) {
            jsUrl.append("customSheet.insertRule('").append(cssRule).append("', ").append(cnt)
                .append(");")
        }
        jsUrl.append("}")
        webView.loadUrl(jsUrl.toString())
    }
}
