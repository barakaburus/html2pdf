package android.print

import android.webkit.WebView
import java.io.File

interface WebViewPdfWriter {

    fun write(webView: WebView, outputPdfFile: File, documentName: String)
}