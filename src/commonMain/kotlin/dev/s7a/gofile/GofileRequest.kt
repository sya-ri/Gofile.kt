package dev.s7a.gofile

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters

/**
 * Request types of Gofile.io.
 */
sealed interface GofileRequest {
    /**
     * Http method.
     */
    val method: HttpMethod

    /**
     * Request url.
     */
    val urlString: String

    /**
     * A process to HttpRequestBuilder.
     */
    fun buildAction(builder: HttpRequestBuilder)

    /**
     * Returns the best server available to receive files.
     */
    object GetServer : GofileRequest {
        override val method = HttpMethod.Get
        override val urlString = "https://api.gofile.io/getServer"
        override fun buildAction(builder: HttpRequestBuilder) {}
    }

    /**
     * Upload one file on a specific server.
     * If you specify a folderId, the file will be added to this folder.
     *
     * @property fileName File name.
     * @property fileContent Bytes of file data
     * @property contentType A two-part identifier for file formats and format contents transmitted on the Internet.
     *                       See also [media-types](https://www.iana.org/assignments/media-types/media-types.xhtml)
     * @property token The access token of an account. Can be retrieved from the profile page.
     *                 If valid, the file will be added to this account.
     *                 If undefined, a guest account will be created to receive the file.
     * @property folderId The ID of a folder.
     *                    If valid, the file will be added to this folder.
     *                    If undefined, a new folder will be created to receive the file.
     *                    When using the folderId, you must pass the account token.
     * @property server Server to upload to.
     */
    class UploadFile(val fileName: String, val fileContent: ByteArray, val contentType: String, val token: String?, val folderId: String?, val server: String) : GofileRequest {
        override val method = HttpMethod.Post
        override val urlString = "https://$server.gofile.io/uploadFile"
        override fun buildAction(builder: HttpRequestBuilder) {
            builder.setBody(
                MultiPartFormDataContent(
                    formData {
                        if (token != null) append("token", token)
                        if (folderId != null) append("folderId", folderId)
                        append(
                            "file",
                            fileContent,
                            Headers.build {
                                append(HttpHeaders.ContentType, contentType)
                                append(HttpHeaders.ContentDisposition, "filename=\"${fileName}\"")
                            }
                        )
                    }
                )
            )
        }
    }

    /**
     * Create a new folder.
     *
     * `https://api.gofile.io/createFolder`
     *
     * @property parentFolderId The parent folder ID.
     * @property folderName The name of the created folder.
     * @property token The access token of an account. Can be retrieved from the profile page.
     */
    class CreateFolder(val parentFolderId: String, val folderName: String, val token: String) : GofileRequest {
        override val method = HttpMethod.Put
        override val urlString = "https://api.gofile.io/createFolder"
        override fun buildAction(builder: HttpRequestBuilder) {
            builder.setBody(
                FormDataContent(
                    Parameters.build {
                        append("parentFolderId", parentFolderId)
                        append("folderName", folderName)
                        append("token", token)
                    }
                )
            )
        }
    }

    /**
     * Set an option on a folder.
     *
     * `https://api.gofile.io/setFolderOption`
     *
     * @property folderId The folder ID.
     * @property option The option.
     * @property token The access token of an account. Can be retrieved from the profile page.
     */
    class SetFolderOption(val folderId: String, val option: GofileFolderOption, val token: String) : GofileRequest {
        override val method = HttpMethod.Put
        override val urlString = "https://api.gofile.io/setFolderOption"
        override fun buildAction(builder: HttpRequestBuilder) {
            builder.setBody(
                FormDataContent(
                    Parameters.build {
                        append("folderId", folderId)
                        append("option", option.name)
                        append("value", option.value)
                        append("token", token)
                    }
                )
            )
        }
    }

    /**
     * Copy one or multiple contents to another folder.
     *
     * `https://api.gofile.io/copyContent`
     *
     * @property contentsId ContentId to copy (files or folders).
     * @property folderIdDest Destination folder ID.
     * @property token The access token of an account. Can be retrieved from the profile page.
     */
    class CopyContent(val contentsId: List<String>, val folderIdDest: String, val token: String) : GofileRequest {
        override val method = HttpMethod.Put
        override val urlString = "https://api.gofile.io/copyContent"
        override fun buildAction(builder: HttpRequestBuilder) {
            builder.setBody(
                FormDataContent(
                    Parameters.build {
                        append("contentsId", contentsId.joinToString(","))
                        append("folderIdDest", folderIdDest)
                        append("token", token)
                    }
                )
            )
        }
    }

    /**
     * Delete one or multiple files/folders.
     *
     * `https://api.gofile.io/deleteContent`
     *
     * @property contentsId ContentId to delete (files or folders).
     * @property token The access token of an account. Can be retrieved from the profile page.
     */
    class DeleteContent(val contentsId: List<String>, val token: String) : GofileRequest {
        override val method = HttpMethod.Delete
        override val urlString = "https://api.gofile.io/deleteContent"
        override fun buildAction(builder: HttpRequestBuilder) {
            builder.setBody(
                FormDataContent(
                    Parameters.build {
                        append("contentsId", contentsId.joinToString(","))
                        append("token", token)
                    }
                )
            )
        }
    }
}
