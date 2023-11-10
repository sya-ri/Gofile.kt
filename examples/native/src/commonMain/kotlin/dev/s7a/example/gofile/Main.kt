package dev.s7a.example.gofile

import dev.s7a.gofile.GofileClient
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import kotlinx.coroutines.runBlocking
import okio.FileSystem
import okio.Path.Companion.toPath
import platform.posix.getenv
import kotlin.system.exitProcess

@OptIn(ExperimentalForeignApi::class)
fun main() {
    val token: String? = getenv("GOFILE_TOKEN")?.toKString()
    val client = GofileClient()
    val (fileName, fileContent) = inputFile()
    runBlocking {
        client.uploadFile(fileName, fileContent, "", token = token).fold(
            onSuccess = {
                println("Download page: ${it.downloadPage}")
            },
            onFailure = {
                it.printStackTrace()
                exitProcess(ExitCode.UploadFailed)
            },
        )
    }
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

private fun inputFile(): Pair<String, ByteArray> {
    print("File path to upload: ")
    val input = readLine() ?: exitProcess(ExitCode.NoInputPath)
    val filePath = input.toPath()
    when {
        FileSystem.SYSTEM.exists(filePath).not() -> {
            println("$filePath: No such file.")
            exitProcess(ExitCode.NoSuchFile)
        }
        FileSystem.SYSTEM.metadata(filePath).isRegularFile.not() -> {
            println("$filePath: Is not file.")
            exitProcess(ExitCode.IsNotFile)
        }
        else -> {
            return filePath.name to FileSystem.SYSTEM.read(filePath) {
                readByteArray()
            }
        }
    }
}
