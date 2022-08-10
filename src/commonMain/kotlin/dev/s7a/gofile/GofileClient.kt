package dev.s7a.gofile

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.request
import io.ktor.serialization.kotlinx.json.json

/**
 * Gofile.io client that uses a pre-setup [HttpClient].
 *
 * @property client [HttpClient]
 */
class GofileClient(private val client: HttpClient) {
    companion object {
        /**
         * Set up [HttpClient] for [GofileClient].
         */
        fun setupClient(client: HttpClientConfig<*>) {
            client.run {
                install(ContentNegotiation) {
                    json()
                }
            }
        }
    }

    /**
     * Gofile.io client that uses an auto-setup [HttpClient].
     */
    constructor() : this(HttpClient(Companion::setupClient))

    /**
     * Gofile.io client that uses an auto-setup [HttpClient].
     *
     * @param engine [HttpClientEngine]
     */
    constructor(engine: HttpClientEngine) : this(HttpClient(engine, Companion::setupClient))

    /**
     * Gofile.io client that uses an auto-setup [HttpClient].
     *
     * @param factory [HttpClientEngineFactory]
     */
    constructor(factory: HttpClientEngineFactory<*>) : this(factory.create())

    private suspend inline fun <reified T> request(request: GofileRequest): Result<T> {
        return runCatching {
            client.request(request.urlString) {
                this.method = request.method
                request.buildAction(this)
            }
        }.mapCatching { response ->
            response to response.body<GofileResponse<T>>()
        }.map { (response, body) ->
            return when (body) {
                is GofileResponse.Ok -> Result.success(body.data)
                is GofileResponse.Error -> Result.failure(GofileStatusException(response, body.status))
            }
        }
    }

    /**
     * Get the best server available to receive files.
     *
     * `https://api.gofile.io/getServer`
     *
     * @see getServerName
     */
    suspend fun getServer(): Result<GofileResponse.GetServer> {
        return request(GofileRequest.GetServer)
    }

    /**
     * Get the best server name available to receive files.
     *
     * @see getServer
     */
    suspend fun getServerName(): String? {
        return getServer().getOrNull()?.server
    }

    /**
     * Upload one file on a specific server.
     * If you specify a folderId, the file will be added to this folder.
     *
     * `https://{server}.gofile.io/uploadFile`
     *
     * @param fileName File name.
     * @param fileContent Bytes of file data
     * @param contentType A two-part identifier for file formats and format contents transmitted on the Internet.
     *                    See also [media-types](https://www.iana.org/assignments/media-types/media-types.xhtml)
     * @param token The access token of an account. Can be retrieved from the profile page.
     *              If valid, the file will be added to this account.
     *              If undefined, a guest account will be created to receive the file.
     * @param folderId The ID of a folder.
     *                 If valid, the file will be added to this folder.
     *                 If undefined, a new folder will be created to receive the file.
     *                 When using the folderId, you must pass the account token.
     * @param server Server to upload to. If you specify null, it will use the best available.
     */
    suspend fun uploadFile(fileName: String, fileContent: ByteArray, contentType: String, token: String? = null, folderId: String? = null, server: String? = null): Result<GofileResponse.UploadFile> {
        val serverName = server ?: getServer().fold(onSuccess = { it.server }, onFailure = { return Result.failure(it) })
        return request(GofileRequest.UploadFile(fileName, fileContent, contentType, token, folderId, serverName))
    }

    /**
     * Create a new folder.
     *
     * `https://api.gofile.io/createFolder`
     *
     * @param parentFolderId The parent folder ID.
     * @param folderName The name of the created folder.
     * @param token The access token of an account. Can be retrieved from the profile page.
     */
    suspend fun createFolder(parentFolderId: String, folderName: String, token: String): Result<GofileResponse.CreateFolder> {
        return request(GofileRequest.CreateFolder(parentFolderId, folderName, token))
    }

    /**
     * Set an option on a folder.
     *
     * `https://api.gofile.io/setFolderOption`
     *
     * @param folderId The folder ID.
     * @param option The option.
     * @param token The access token of an account. Can be retrieved from the profile page.
     */
    suspend fun setFolderOption(folderId: String, option: GofileFolderOption, token: String): Result<Unit> {
        return request(GofileRequest.SetFolderOption(folderId, option, token))
    }

    /**
     * Copy one or multiple contents to another folder.
     *
     * `https://api.gofile.io/copyContent`
     *
     * @param contentId ContentId to copy (files or folders).
     * @param folderIdDest Destination folder ID.
     * @param token The access token of an account. Can be retrieved from the profile page.
     */
    suspend fun copyContent(contentId: String, folderIdDest: String, token: String): Result<Unit> {
        return copyContent(listOf(contentId), folderIdDest, token)
    }

    /**
     * Copy one or multiple contents to another folder.
     *
     * `https://api.gofile.io/copyContent`
     *
     * @param contentsId ContentId to copy (files or folders).
     * @param folderIdDest Destination folder ID.
     * @param token The access token of an account. Can be retrieved from the profile page.
     */
    suspend fun copyContent(contentsId: List<String>, folderIdDest: String, token: String): Result<Unit> {
        return request(GofileRequest.CopyContent(contentsId, folderIdDest, token))
    }

    /**
     * Delete one or multiple files/folders.
     *
     * `https://api.gofile.io/deleteContent`
     *
     * @param contentId ContentId to delete (files or folders).
     * @param token The access token of an account. Can be retrieved from the profile page.
     */
    suspend fun deleteContent(contentId: String, token: String): Result<Unit> {
        return deleteContent(listOf(contentId), token)
    }

    /**
     * Delete one or multiple files/folders.
     *
     * `https://api.gofile.io/deleteContent`
     *
     * @param contentsId ContentId to delete (files or folders).
     * @param token The access token of an account. Can be retrieved from the profile page.
     */
    suspend fun deleteContent(contentsId: List<String>, token: String): Result<Unit> {
        return request(GofileRequest.DeleteContent(contentsId, token))
    }
}
