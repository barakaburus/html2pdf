package com.aburus.html2pdf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.coroutineScope

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val html2Pdf = Html2Pdf(this)

        findViewById<Button>(R.id.button).setOnClickListener {
            lifecycleScope.launchWhenCreated {
                val html = "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<body>\n" +
                        "\n" +
                        "<h1>My First Heading</h1>\n" +
                        "<p>My first paragraph.</p>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>"

                html2Pdf.convert(html, "Simple Html")
            }
        }
    }
}