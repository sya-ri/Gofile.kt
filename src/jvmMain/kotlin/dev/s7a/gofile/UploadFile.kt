package dev.s7a.gofile

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path

/**
 * Upload one file on a specific server.
 * If you specify a folderId, the file will be added to this folder.
 *
 * `https://{server}.gofile.io/uploadFile`
 *
 * @param file File to upload. It will get contentType based on this.
 * @param token The access token of an account. Can be retrieved from the profile page.
 *              If valid, the file will be added to this account.
 *              If undefined, a guest account will be created to receive the file.
 * @param folderId The ID of a folder.
 *                 If valid, the file will be added to this folder.
 *                 If undefined, a new folder will be created to receive the file.
 *                 When using the folderId, you must pass the account token.
 * @param server Server to upload to. If you specify null, it will use the best available.
 */
suspend fun GofileClient.uploadFile(file: File, token: String? = null, folderId: String? = null, server: String? = null): Result<GofileUploadFileResponse> {
    val contentType = withContext(Dispatchers.IO) {
        Files.probeContentType(Path(file.name))
    } ?: return Result.failure(UnknownContentTypeException(file))
    return uploadFile(file, contentType, token, folderId, server)
}

/**
 * Upload one file on a specific server.
 * If you specify a folderId, the file will be added to this folder.
 *
 * `https://{server}.gofile.io/uploadFile`
 *
 * @param file File to upload.
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
suspend fun GofileClient.uploadFile(file: File, contentType: String, token: String? = null, folderId: String? = null, server: String? = null): Result<GofileUploadFileResponse> {
    return uploadFile(file.name, file.readBytes(), contentType, token, folderId, server)
}
