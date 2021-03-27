package com.aburus.html2pdf

import android.content.Context
import android.net.Uri
import android.print.PrintAttributes
import android.print.WebViewPdfWriter
import android.print.WebViewPdfWriterImpl
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val FOLDER_NAME = "html2pdf"

class HtmlPdfConverter {

    private val webViewPdfWriter: WebViewPdfWriter = WebViewPdfWriterImpl(
        PrintAttributes.Builder()
            .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
            .setMediaSize(PrintAttributes.MediaSize.NA_LETTER)
            .setResolution(
                PrintAttributes.Resolution(
                    "Resolution_ID",
                    Context.PRINT_SERVICE,
                    300,
                    300
                )
            )
            .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
            .build()
    )

    suspend fun convert(context: Context, html: String, documentName: String): Uri {
        val folder = File(context.filesDir, FOLDER_NAME)
        val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val file = File(folder, "$timestamp.pdf")

        convert(context, html, file, documentName)

        return FileProvider.getUriForFile(context, Html2Pdf.fileProviderAuthority, file)
    }

    suspend fun convert(context: Context, html: String, outputPdfFile: File, documentName: String) {
        withContext(Dispatchers.Main) {
            WebView(context).apply {
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) = false

                    override fun onPageFinished(view: WebView?, url: String?) {
                        view?.let { webViewPdfWriter.write(view, outputPdfFile, documentName) }
                        super.onPageFinished(view, url)
                    }
                }

                loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
            }
        }
    }
}