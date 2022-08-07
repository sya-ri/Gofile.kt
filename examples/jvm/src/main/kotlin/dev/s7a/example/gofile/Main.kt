@file:JvmName("Main")

package dev.s7a.example.gofile

import dev.s7a.gofile.GofileClient
import dev.s7a.gofile.uploadFile
import kotlin.io.path.createTempFile
import kotlin.io.path.writeText

suspend fun main() {
    val client = GofileClient()
    val file = createTempFile(suffix = ".txt").apply {
        writeText("Hello world.")
    }.toFile()
    client.uploadFile(file).fold(
        onSuccess = { println("Download page: ${it.downloadPage}") },
        onFailure = Throwable::printStackTrace
    )
}
