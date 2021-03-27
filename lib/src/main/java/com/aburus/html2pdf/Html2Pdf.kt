package com.aburus.html2pdf

import android.content.Context
import android.net.Uri

private const val fileProviderAuthoritySuffix = ".com.aburus.html2pdf.provider"

class Html2Pdf(private val context: Context) {

    private val htmlPdfConverter = HtmlPdfConverter()

    init {
        fileProviderAuthority = context.packageName + fileProviderAuthoritySuffix
    }

    suspend fun convert(html: String, documentName: String): Uri {
        return htmlPdfConverter.convert(context, html, documentName)
    }

    companion object {
        internal lateinit var fileProviderAuthority: String
    }
}