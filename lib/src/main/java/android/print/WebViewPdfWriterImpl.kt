package android.print

import android.os.Build
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.util.Log
import android.webkit.WebView
import java.io.File

private const val TAG = "WebViewPdfWriterImpl"

/**
 * Creates a pdf file from a WebView content.
 * IMPORTANT NOTE: This class must reside in the "android.print" package in order to work.
 */
class WebViewPdfWriterImpl(
        private val printAttributes: PrintAttributes,
) : WebViewPdfWriter {

    override fun write(webView: WebView, outputPdfFile: File, documentName: String) {
        val printAdapter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.createPrintDocumentAdapter(documentName)
        } else {
            webView.createPrintDocumentAdapter()
        }

        printAdapter.onLayout(null, printAttributes, null, object : PrintDocumentAdapter.LayoutResultCallback() {

            override fun onLayoutFinished(info: PrintDocumentInfo, changed: Boolean) {
                printAdapter.onWrite(arrayOf(PageRange.ALL_PAGES), getOutputFile(outputPdfFile), CancellationSignal(), object : PrintDocumentAdapter.WriteResultCallback() {})
            }
        }, null)
    }

    private fun getOutputFile(outputPdfFile: File): ParcelFileDescriptor? {
        try {
            if (outputPdfFile.exists()) {
                outputPdfFile.delete()
            } else {
                outputPdfFile.parentFile?.let { parentFile ->
                    if (!parentFile.exists()) {
                        parentFile.mkdirs()
                    }
                }
            }

            outputPdfFile.createNewFile()
            return ParcelFileDescriptor.open(outputPdfFile, ParcelFileDescriptor.MODE_READ_WRITE)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            e.printStackTrace()
        }
        return null
    }
}