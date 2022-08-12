package dev.s7a.gofile

import kotlinx.serialization.Serializable

/**
 * @property id An id of the created folder.
 * @property type Content type.
 * @property name A name of the created folder.
 * @property parentFolder The parent folder id of the created folder.
 * @property createTime Creation time.
 * @property childs Files and folders the folder.
 * @property code A code to open the folder.
 */
@Serializable
data class GofileCreateFolderResponse(
    val id: String,
    val type: GofileContentType,
    val name: String,
    val parentFolder: String,
    val createTime: Long,
    val childs: List<String>,
    val code: String
)
