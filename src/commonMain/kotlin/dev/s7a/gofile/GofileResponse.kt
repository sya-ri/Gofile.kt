package dev.s7a.gofile

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Response types of Gofile.io.
 */
@Serializable(with = GofileResponseSerializer::class)
sealed class GofileResponse<out T> {
    /**
     * If Gofile.io returns "ok" status, you can handle data.
     */
    @Serializable
    data class Ok<out T>(val data: T) : GofileResponse<T>()

    /**
     * Gofile.io returned some error.
     */
    @Serializable
    data class Error(val status: String, val data: JsonObject? = null) : GofileResponse<Nothing>()

    /**
     * @property server The best server available to receive files.
     */
    @Serializable
    data class GetServer(val server: String)

    /**
     * @property downloadPage A url to download the uploaded file.
     * @property code A code to download the uploaded file.
     * @property parentFolder The parent folder id of the uploaded file.
     * @property fileId A id of the uploaded file.
     * @property fileName A name of the upload file.
     * @property md5 A checksum of the uploaded file.
     * @property guestToken If you don't specify a token in the request, Gofile.io will create a guest token.
     */
    @Serializable
    data class UploadFile(val downloadPage: String, val code: String, val parentFolder: String, val fileId: String, val fileName: String, val md5: String, val guestToken: String? = null)

    /**
     * @property id A id of the created folder.
     * @property type Content type.
     * @property name A name of the created folder.
     * @property parentFolder The parent folder id of the created folder.
     * @property createTime Creation time.
     * @property childs Files and folders the folder.
     * @property code A code to open the folder.
     */
    @Serializable
    data class CreateFolder(val id: String, val type: String, val name: String, val parentFolder: String, val createTime: Long, val childs: List<String>, val code: String)

    /**
     * **The statistics are updated every 24 hours.**
     *
     * @property token The access token of an account.
     * @property email The email of an account.
     * @property tier The tier of an account.
     * @property rootFolder The root folder id.
     * @property filesCount A number of files.
     * @property filesCountLimit
     * @property totalSize A size of all files.
     * @property totalSizeLimit
     * @property total30DDLTraffic
     * @property total30DDLTrafficLimit
     */
    @Serializable
    data class GetAccountDetails(val token: String, val email: String, val tier: GofileTier, val rootFolder: String, val filesCount: Int, val filesCountLimit: Int?, val totalSize: Int, val totalSizeLimit: Int?, val total30DDLTraffic: Int, val total30DDLTrafficLimit: Int?)
}
