@file:JvmName("Main")

package dev.s7a.example.gofile

import dev.s7a.gofile.GofileClient
import dev.s7a.gofile.uploadFile
import java.io.File
import kotlin.system.exitProcess

suspend fun main() {
    val token: String? = System.getenv("GOFILE_TOKEN")
    val client = GofileClient()
    val file = inputFile()
    client.uploadFile(file, token = token).fold(
        onSuccess = {
            println("Download page: ${it.downloadPage}")
        },
        onFailure = {
            it.printStackTrace()
            exitProcess(ExitCode.UploadFailed)
        },
    )
}

private enum class ExitCode {
    NoInputPath,
    NoSuchFile,
    IsNotFile,
    UploadFailed,
}

private fun exitProcess(exitCode: ExitCode): Nothing {
    exitProcess(exitCode.ordinal + 1)
}

private fun inputFile(): File {
    print("File path to upload: ")
    val filePath = readLine() ?: exitProcess(ExitCode.NoInputPath)
    return File(filePath).apply {
        when {
            exists().not() -> {
                println("$filePath: No such file.")
                exitProcess(ExitCode.NoSuchFile)
            }
            isFile.not() -> {
                println("$filePath: Is not file.")
                exitProcess(ExitCode.IsNotFile)
            }
        }
    }
}
