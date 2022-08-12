package dev.s7a.gofile

import kotlinx.serialization.Serializable

/**
 * @see GofileContent.Folder.contents
 */
@Serializable(with = GofileChildContentSerializer::class)
sealed class GofileChildContent {
    /**
     * Content id.
     */
    abstract val id: String

    /**
     * Content type.
     */
    abstract val type: GofileContentType

    /**
     * Name.
     */
    abstract val name: String

    /**
     * Content id of the parent folder.
     */
    abstract val parentFolder: String

    /**
     * Create time.
     */
    abstract val createTime: Long

    /**
     * @property size File size.
     * @property downloadCount Total number of downloads.
     * @property md5 Checksum of the file.
     * @property mimetype Content-type.
     * @property serverChoosen Server name using for the direct link.
     * @property directLink Download link.
     * @property link Download page link.
     * @property thumbnail Thumbnail link.
     */
    data class File(
        override val id: String,
        override val name: String,
        override val parentFolder: String,
        override val createTime: Long,
        val size: Double,
        val downloadCount: Int,
        val md5: String,
        val mimetype: String,
        val serverChoosen: String,
        val directLink: String,
        val link: String,
        val thumbnail: String? = null
    ) : GofileChildContent() {
        override val type = GofileContentType.File
    }

    /**
     * @property childs File or folder id list.
     * @property code Code of the folder.
     * @property public Whether anyone can access it.
     * @property password Whether a password is required for access.
     * @property description Description.
     * @property expire Expiration date in the form of unix timestamp.
     * @property tags Tags.
     */
    data class Folder(
        override val id: String,
        override val name: String,
        override val parentFolder: String,
        override val createTime: Long,
        val childs: List<String>,
        val code: String,
        val public: Boolean,
        val password: Boolean = false,
        val description: String? = null,
        val expire: Long? = null,
        val tags: List<String> = emptyList()
    ) : GofileChildContent() {
        override val type = GofileContentType.Folder
    }
}
