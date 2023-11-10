@file:JvmName("Main")

package dev.s7a.example.gofile

import dev.s7a.gofile.GofileClient
import dev.s7a.gofile.GofileOption
import dev.s7a.gofile.GofileTier
import dev.s7a.gofile.uploadFile
import java.util.Calendar
import kotlin.io.path.createTempFile
import kotlin.io.path.writeText

suspend fun main() {
    val client = GofileClient()
    var token: String? = System.getenv("GOFILE_TOKEN")
    val file = createTempFile(suffix = ".txt").apply {
        writeText("Hello world.")
    }.toFile()
    val uploadFile = client.uploadFile(file, token = token).getOrThrow()
    println("uploadFile: $uploadFile")
    if (token == null) token = uploadFile.guestToken ?: return
    val accountDetails = client.getAccountDetails(token).getOrThrow()
    println("accountDetails: $accountDetails")
    val createFolder = client.createFolder(uploadFile.parentFolder, "new-folder", token).getOrThrow()
    println("createFolder: $createFolder")
    client.setOption(createFolder.id, GofileOption.Description("description"), token).getOrThrow()
    client.setOption(createFolder.id, GofileOption.Expire(Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }.toInstant().epochSecond), token).getOrThrow()
    client.setOption(createFolder.id, GofileOption.Password("password"), token).getOrThrow()
    client.setOption(createFolder.id, GofileOption.Tags("t", "a", "g", "s"), token).getOrThrow()
    if (accountDetails.tier == GofileTier.Donor) {
        client.copyContent(uploadFile.fileId, createFolder.id, token).getOrThrow()
        val getContentFile = client.getContent(uploadFile.fileId, token).getOrThrow()
        println("getContentFile: $getContentFile")
        val getContentFolder = client.getContent(createFolder.id, token).getOrThrow()
        println("getContentFolder: $getContentFolder")
    }
    val deleteContent = client.deleteContent(uploadFile.fileId, token).getOrThrow()
    println("deleteContent: $deleteContent")
    val deleteFolder = client.deleteContent(createFolder.id, token).getOrThrow()
    println("deleteFolder: $deleteFolder")
}
