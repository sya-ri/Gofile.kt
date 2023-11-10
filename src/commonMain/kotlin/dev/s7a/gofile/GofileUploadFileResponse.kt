package dev.s7a.gofile

import kotlinx.serialization.Serializable

/**
 * @property downloadPage A url to download the uploaded file.
 * @property code A code to download the uploaded file.
 * @property parentFolder The parent folder id of the uploaded file.
 * @property fileId A id of the uploaded file.
 * @property fileName A name of the upload file.
 * @property md5 A checksum of the uploaded file.
 * @property guestToken If you don't specify a token in the request, Gofile.io will create a guest token.
 * @see GofileClient.uploadFile
 */
@Serializable
public data class GofileUploadFileResponse(
    val downloadPage: String,
    val code: String,
    val parentFolder: String,
    val fileId: String,
    val fileName: String,
    val md5: String,
    val guestToken: String? = null,
)
