package dev.s7a.gofile

import kotlinx.serialization.Serializable

/**
 * Response types of Gofile.io.
 */
sealed interface GofileResponse<T> {
    /**
     * Response status.
     */
    val status: String

    /**
     * Response data.
     */
    val data: T?

    /**
     * Get whether [status] equals ok.
     */
    val isOk
        get() = status == "ok"

    /**
     * Returns the best server available to receive files.
     *
     * `https://api.gofile.io/getServer`
     */
    @Serializable
    data class GetServer(override val status: String, override val data: Data?) : GofileResponse<GetServer.Data> {
        /**
         * @property server The best server available to receive files.
         */
        @Serializable
        data class Data(val server: String)
    }

    /**
     * Returns the file upload result.
     *
     * `https://{server}.gofile.io/uploadFile`
     */
    @Serializable
    data class UploadFile(override val status: String, override val data: Data?) : GofileResponse<UploadFile.Data> {
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
        data class Data(val downloadPage: String, val code: String, val parentFolder: String, val fileId: String, val fileName: String, val md5: String, val guestToken: String? = null)
    }

    /**
     * Create a new folder.
     *
     * `https://api.gofile.io/createFolder`
     */
    @Serializable
    data class CreateFolder(override val status: String, override val data: Unit?) : GofileResponse<Unit>

    /**
     * Set an option on a folder.
     *
     * `https://api.gofile.io/setFolderOption`
     */
    @Serializable
    data class SetFolderOption(override val status: String, override val data: Unit?) : GofileResponse<Unit>
}
