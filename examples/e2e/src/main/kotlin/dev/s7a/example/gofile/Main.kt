@file:JvmName("Main")

package dev.s7a.example.gofile

import dev.s7a.gofile.GofileClient
import dev.s7a.gofile.GofileFolderOption
import dev.s7a.gofile.uploadFile
import kotlin.io.path.createTempFile
import kotlin.io.path.writeText

suspend fun main() {
    val client = GofileClient()
    var token: String? = System.getenv("GOFILE_TOKEN")
    val file = createTempFile(suffix = ".txt").apply {
        writeText("Hello world.")
    }.toFile()
    val uploadFile = client.uploadFile(file, token = token).getOrThrow()
    println("Download page: ${uploadFile.downloadPage}")
    if (token == null) token = uploadFile.guestToken ?: return
    val createFolder = client.createFolder(uploadFile.parentFolder, "new-folder", token).getOrThrow()
    client.setFolderOption(createFolder.id, GofileFolderOption.Description("description"), token).getOrThrow()
    client.setFolderOption(createFolder.id, GofileFolderOption.Expire(1660106197), token).getOrThrow()
    client.setFolderOption(createFolder.id, GofileFolderOption.Password("password"), token).getOrThrow()
    client.setFolderOption(createFolder.id, GofileFolderOption.Tags("t", "a", "g", "s"), token).getOrThrow()
    // Only premium user
    // client.copyContent(uploadFile.fileId, createFolder.id, token).getOrThrow()
    client.deleteContent(uploadFile.fileId, token).getOrThrow()
}
