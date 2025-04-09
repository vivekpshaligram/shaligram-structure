package com.codestracture.ui.epub

import android.util.Log
import fi.iki.elonen.NanoHTTPD
import java.io.File

class LocalServer(port: Int, private val fileDirectory: File) : NanoHTTPD("127.0.0.1", port) {

    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri
        val file = File(fileDirectory, uri.trimStart('/'))
        Log.d("MyTag", "file::${file.path}")

        return try {
            if (file.exists() && file.isFile) {
                Log.d("MyTag", "file if")
                val contentType = when {
                    uri.endsWith(".html") -> "text/html"
                    uri.endsWith(".css") -> "text/css"
                    uri.endsWith(".js") -> "application/javascript"
                    uri.endsWith(".png") -> "image/png"
                    uri.endsWith(".jpg") || uri.endsWith(".jpeg") -> "image/jpeg"
                    else -> "text/plain"
                }

                val fileContent = file.readText()
                newFixedLengthResponse(Response.Status.OK, contentType, fileContent)
            } else {
                Log.d("MyTag", "file else")
                newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "File not found")
            }
        } catch (e: Exception) {
            Log.d("MyTag", "catch Exception::${e.message}")
            newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "File not found")
        }
    }
}
