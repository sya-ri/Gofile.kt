package dev.s7a.gofile

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.request
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Gofile.io client that uses a pre-setup [HttpClient].
 *
 * @property client [HttpClient]
 */
public class GofileClient(private val client: HttpClient) {
    public companion object {
        /**
         * Set up [HttpClient] for [GofileClient].
         */
        public fun setupClient(client: HttpClientConfig<*>) {
            client.run {
                install(ContentNegotiation) {
                    json(
                        Json {
                            ignoreUnknownKeys = true
                        },
                    )
                }
            }
        }
    }

    /**
     * Gofile.io client that uses an auto-setup [HttpClient].
     */
    public constructor() : this(HttpClient(Companion::setupClient))

    /**
     * Gofile.io client that uses an auto-setup [HttpClient].
     *
     * @param engine [HttpClientEngine]
     */
    public constructor(engine: HttpClientEngine) : this(HttpClient(engine, Companion::setupClient))

    /**
     * Gofile.io client that uses an auto-setup [HttpClient].
     *
     * @param factory [HttpClientEngineFactory]
     */
    public constructor(factory: HttpClientEngineFactory<*>) : this(factory.create())

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
     */
    public suspend fun getServer(): Result<GofileGetServerResponse> {
        return request(GofileRequest.GetServer)
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
    public suspend fun uploadFile(fileName: String, fileContent: ByteArray, contentType: String, token: String? = null, folderId: String? = null, server: String? = null): Result<GofileUploadFileResponse> {
        val serverName = server ?: getServer().fold(onSuccess = { it.server }, onFailure = { return Result.failure(it) })
        return request(GofileRequest.UploadFile(fileName, fileContent, contentType, token, folderId, serverName))
    }

    /**
     * Get a specific content details. **Only available to premium users.**
     *
     * `https://api.gofile.io/getContent`
     *
     * @param contentId The content ID.
     * @param token The access token of an account. Can be retrieved from the profile page.
     */
    public suspend fun getContent(contentId: String, token: String): Result<GofileContent> {
        return request(GofileRequest.GetContent(contentId, token))
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
    public suspend fun createFolder(parentFolderId: String, folderName: String, token: String): Result<GofileCreateFolderResponse> {
        return request(GofileRequest.CreateFolder(parentFolderId, folderName, token))
    }

    /**
     * Set an option on a folder or file.
     *
     * `https://api.gofile.io/setOption`
     *
     * @param contentId The contet ID.
     * @param option The option.
     * @param token The access token of an account. Can be retrieved from the profile page.
     */
    public suspend fun setOption(contentId: String, option: GofileOption, token: String): Result<Unit> {
        return request(GofileRequest.SetOption(contentId, option, token))
    }

    /**
     * Copy one or multiple contents to another folder. **Only available to premium users.**
     *
     * `https://api.gofile.io/copyContent`
     *
     * @param contentId ContentId to copy (files or folders).
     * @param folderIdDest Destination folder ID.
     * @param token The access token of an account. Can be retrieved from the profile page.
     */
    public suspend fun copyContent(contentId: String, folderIdDest: String, token: String): Result<Unit> {
        return copyContent(listOf(contentId), folderIdDest, token)
    }

    /**
     * Copy one or multiple contents to another folder. **Only available to premium users.**
     *
     * `https://api.gofile.io/copyContent`
     *
     * @param contentsId ContentId to copy (files or folders).
     * @param folderIdDest Destination folder ID.
     * @param token The access token of an account. Can be retrieved from the profile page.
     */
    public suspend fun copyContent(contentsId: List<String>, folderIdDest: String, token: String): Result<Unit> {
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
    public suspend fun deleteContent(contentId: String, token: String): Result<Map<String, String>> {
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
    public suspend fun deleteContent(contentsId: List<String>, token: String): Result<Map<String, String>> {
        return request(GofileRequest.DeleteContent(contentsId, token))
    }

    /**
     * Retrieving specific account information. **The statistics are updated every 24 hours.**
     *
     * `https://api.gofile.io/getAccountDetails`
     *
     * @param token The access token of an account. Can be retrieved from the profile page.
     */
    public suspend fun getAccountDetails(token: String): Result<GofileGetAccountDetailsResponse> {
        return request(GofileRequest.GetAccountDetails(token))
    }
}
